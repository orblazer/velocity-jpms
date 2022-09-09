package org.example.jpms.app.plugin.loader;

import org.example.jpms.api.plugin.PluginContainer;
import org.example.jpms.api.plugin.PluginDescription;

import java.util.Optional;

public class PluginContainerImpl implements PluginContainer {
    private final PluginDescription description;
    private Object instance;

    public PluginContainerImpl(PluginDescription description) {
        this.description = description;
    }

    @Override
    public PluginDescription getDescription() {
        return this.description;
    }

    @Override
    public Optional<?> getInstance() {
        return Optional.ofNullable(instance);
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
