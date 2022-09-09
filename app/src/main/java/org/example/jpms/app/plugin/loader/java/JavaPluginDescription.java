package org.example.jpms.app.plugin.loader.java;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.jpms.api.plugin.meta.PluginDependency;
import org.example.jpms.app.plugin.loader.PluginDescriptionImpl;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

class JavaPluginDescription extends PluginDescriptionImpl {
    private final Module module;
    private final Class<?> mainClass;

    JavaPluginDescription(String id, @Nullable String name, @Nullable String version,
                          @Nullable String description, @Nullable String url, @Nullable List<String> authors,
                          Collection<PluginDependency> dependencies, Module module, Class<?> mainClass) {
        super(id, name, version, description, url, authors, dependencies);
        this.module = checkNotNull(module);
        this.mainClass = checkNotNull(mainClass);
    }

    Module getModule() {
        return module;
    }

    Class<?> getMainClass() {
        return mainClass;
    }
}
