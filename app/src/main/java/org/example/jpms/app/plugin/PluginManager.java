package org.example.jpms.app.plugin;

import com.google.common.base.Joiner;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jpms.api.plugin.PluginContainer;
import org.example.jpms.api.plugin.PluginDescription;
import org.example.jpms.api.plugin.meta.PluginDependency;
import org.example.jpms.app.App;
import org.example.jpms.app.plugin.loader.PluginContainerImpl;
import org.example.jpms.app.plugin.loader.java.JavaPluginLoader;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class PluginManager {
    private static final Logger logger = LogManager.getLogger(PluginManager.class);

    private final Map<String, PluginContainer> pluginsById = new LinkedHashMap<>();
    private final App app;

    public PluginManager(App app) {
        this.app = checkNotNull(app);
    }

    /**
     * Loads all plugins from the specified {@code directory}.
     *
     * @param directory the directory to load from
     */
    public void loadPlugins(Path directory) {
        checkNotNull(directory, "directory");
        checkArgument(directory.toFile().isDirectory(), "provided path isn't a directory");

        JavaPluginLoader loader = new JavaPluginLoader(app, directory);
        ModuleLayer parent = ModuleLayer.boot();

        // Search for plugins in the plugins directory
        final ModuleFinder pluginsFinder = ModuleFinder.of(directory);

        // Find all names of all found plugin modules
        List<String> pluginNames = pluginsFinder.findAll().stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name).toList();

        // Create configuration that will resolve and bind plugin modules
        Configuration pluginsConfiguration = parent.configuration()
                .resolveAndBind(ModuleFinder.of(), pluginsFinder, pluginNames);

        // Create a module layer for plugins
        ModuleLayer layer = parent.defineModulesWithOneLoader(pluginsConfiguration, getClass().getClassLoader());

        // List all plugins
        Set<java.lang.Module> plugins = layer.modules();

        if (plugins.isEmpty()) {
            // No plugins found
            return;
        }

        List<PluginDescription> found = new ArrayList<>();
        for (java.lang.Module module : plugins) {
            try {
                found.add(loader.loadCandidate(module));
            } catch (Exception e) {
                logger.error("Unable to load plugin {}", module.getName(), e);
            }
        }
        List<PluginDescription> sortedPlugins = PluginDependencyUtils.sortCandidates(found);

        Set<String> loadedPluginsById = new HashSet<>();
        Map<PluginContainer, Module> pluginContainers = new LinkedHashMap<>();
        // Now load the plugins
        pluginLoad:
        for (PluginDescription candidate : sortedPlugins) {
            // Verify dependencies
            for (PluginDependency dependency : candidate.getDependencies()) {
                if (!dependency.isOptional() && !loadedPluginsById.contains(dependency.getId())) {
                    logger.error("Can't load plugin {} due to missing dependency {}", candidate.getId(),
                            dependency.getId());
                    continue pluginLoad;
                }
            }

            try {
                PluginDescription realPlugin = loader.createPluginFromCandidate(candidate);
                PluginContainerImpl container = new PluginContainerImpl(realPlugin);
                pluginContainers.put(container, loader.createModule(container));
                loadedPluginsById.add(realPlugin.getId());
            } catch (Exception e) {
                logger.error("Can't create module for plugin {}", candidate.getId(), e);
            }
        }

        // Make a global Guice module that with common bindings for every plugin
        AbstractModule commonModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(App.class).toInstance(app);
                for (PluginContainer container : pluginContainers.keySet()) {
                    bind(PluginContainer.class)
                            .annotatedWith(Names.named(container.getDescription().getId()))
                            .toInstance(container);
                }
            }
        };

        for (Map.Entry<PluginContainer, Module> plugin : pluginContainers.entrySet()) {
            PluginContainer container = plugin.getKey();
            PluginDescription description = container.getDescription();

            try {
                loader.createPlugin(container, plugin.getValue(), commonModule);
            } catch (Exception e) {
                logger.error("Can't create plugin {}", description.getId(), e);
                continue;
            }

            logger.info("Loaded plugin {} {} by {}", description.getId(), description.getVersion()
                    .orElse("<UNKNOWN>"), Joiner.on(", ").join(description.getAuthors()));
            registerPlugin(container);
        }
    }

    private void registerPlugin(PluginContainer plugin) {
        pluginsById.put(plugin.getDescription().getId(), plugin);
    }

    public Collection<PluginContainer> getPlugins() {
        return Collections.unmodifiableCollection(pluginsById.values());
    }
}
