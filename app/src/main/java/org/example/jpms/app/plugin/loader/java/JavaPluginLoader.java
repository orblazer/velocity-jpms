package org.example.jpms.app.plugin.loader.java;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.example.jpms.api.plugin.Dependency;
import org.example.jpms.api.plugin.Plugin;
import org.example.jpms.api.plugin.PluginContainer;
import org.example.jpms.api.plugin.PluginDescription;
import org.example.jpms.api.plugin.meta.PluginDependency;
import org.example.jpms.app.App;
import org.example.jpms.app.plugin.loader.PluginContainerImpl;

import java.lang.module.ModuleDescriptor;
import java.nio.file.Path;
import java.util.*;

public class JavaPluginLoader {
    private final App app;
    private final Path baseDirectory;

    public JavaPluginLoader(App app, Path baseDirectory) {
        this.app = app;
        this.baseDirectory = baseDirectory;
    }


    public PluginDescription loadCandidate(Module source) throws Exception {
        Plugin plugin = source.getClass().getAnnotation(Plugin.class);
        if (plugin == null) {
            throw new Exception("Invalid plugin, no @Plugin on " + source.getClass().getCanonicalName());
        }

        Set<PluginDependency> dependencies = new HashSet<>();

        for (Dependency dependency : plugin.dependencies()) {
            dependencies.add(toDependencyMeta(dependency));
        }

        return new JavaPluginDescriptionCandidate(
                source.getName(),
                plugin.name(),
                Optional.of(plugin.version()).or(() -> source.getDescriptor().version().map(ModuleDescriptor.Version::toString)).orElse(""),
                plugin.description(),
                plugin.url(),
                List.of(plugin.authors()),
                dependencies,
                source,
                plugin.main()
        );
    }

    public PluginDescription createPluginFromCandidate(PluginDescription candidate) {
        if (!(candidate instanceof JavaPluginDescriptionCandidate description)) {
            throw new IllegalArgumentException("Description provided isn't of the Java plugin loader");
        }

        return new JavaPluginDescription(
                description.getId(),
                description.getName().orElse(null),
                description.getVersion().orElse(null),
                description.getDescription().orElse(null),
                description.getUrl().orElse(null),
                description.getAuthors(),
                description.getDependencies(),
                description.getModule(),
                description.getMainClass()
        );
    }

    public com.google.inject.Module createModule(PluginContainer container) {
        PluginDescription description = container.getDescription();
        if (!(description instanceof JavaPluginDescription javaDescription)) {
            throw new IllegalArgumentException("Description provided isn't of the Java plugin loader");
        }

        return new PluginModule(app, javaDescription, container, baseDirectory);
    }

    public void createPlugin(PluginContainer container, com.google.inject.Module... modules) {
        if (!(container instanceof PluginContainerImpl)) {
            throw new IllegalArgumentException("Container provided isn't of the Java plugin loader");
        }
        PluginDescription description = container.getDescription();
        if (!(description instanceof JavaPluginDescription)) {
            throw new IllegalArgumentException("Description provided isn't of the Java plugin loader");
        }

        Injector injector = Guice.createInjector(modules);
        Object instance = injector
                .getInstance(((JavaPluginDescription) description).getMainClass());

        if (instance == null) {
            throw new IllegalStateException(
                    "Got nothing from injector for plugin " + description.getId());
        }

        ((PluginContainerImpl) container).setInstance(instance);
    }


    private static PluginDependency toDependencyMeta(Dependency dependency) {
        return new PluginDependency(
                dependency.id(),
                null, // TODO Implement version matching in dependency annotation
                dependency.optional()
        );
    }
}
