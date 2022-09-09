package org.example.jpms.app.plugin.loader.java;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.example.jpms.api.plugin.PluginContainer;
import org.example.jpms.api.plugin.PluginDescription;
import org.example.jpms.api.plugin.annotation.DataDirectory;
import org.example.jpms.app.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class PluginModule implements Module {
    private final App app;
    private final JavaPluginDescription description;
    private final PluginContainer pluginContainer;
    private final Path basePluginPath;

    PluginModule(App app, JavaPluginDescription description, PluginContainer pluginContainer, Path basePluginPath) {
        this.app = app;
        this.description = description;
        this.pluginContainer = pluginContainer;
        this.basePluginPath = basePluginPath;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(description.getMainClass()).in(Scopes.SINGLETON);

        binder.bind(Logger.class).toInstance(LoggerFactory.getLogger(description.getId()));
        binder.bind(Path.class).annotatedWith(DataDirectory.class)
                .toInstance(basePluginPath.resolve(description.getId()));
        binder.bind(PluginDescription.class).toInstance(description);
        binder.bind(PluginContainer.class).toInstance(pluginContainer);
    }
}
