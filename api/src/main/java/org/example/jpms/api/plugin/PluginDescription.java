package org.example.jpms.api.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.example.jpms.api.plugin.meta.PluginDependency;

import java.lang.module.ModuleDescriptor;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Represents metadata for a specific version of a plugin.
 */
public interface PluginDescription {
    /**
     * Gets the qualified ID of the {@link Plugin} within this container.
     *
     * @return the plugin ID
     * @see ModuleDescriptor#name()
     */
    String getId();

    /**
     * Gets the name of the {@link Plugin} within this container.
     *
     * @return an {@link Optional} with the plugin name, may be empty
     * @see Plugin#name()
     */
    default Optional<String> getName() {
        return Optional.empty();
    }

    /**
     * Gets the version of the {@link Plugin} within this container.
     *
     * @return an {@link Optional} with the plugin version, may be empty
     * @see Plugin#version()
     */
    default Optional<String> getVersion() {
        return Optional.empty();
    }

    /**
     * Gets the description of the {@link Plugin} within this container.
     *
     * @return an {@link Optional} with the plugin description, may be empty
     * @see Plugin#description()
     */
    default Optional<String> getDescription() {
        return Optional.empty();
    }

    /**
     * Gets the url or website of the {@link Plugin} within this container.
     *
     * @return an {@link Optional} with the plugin url, may be empty
     * @see Plugin#url()
     */
    default Optional<String> getUrl() {
        return Optional.empty();
    }

    /**
     * Gets the authors of the {@link Plugin} within this container.
     *
     * @return the plugin authors, may be empty
     * @see Plugin#authors()
     */
    default List<String> getAuthors() {
        return ImmutableList.of();
    }

    /**
     * Gets a {@link Collection} of all dependencies of the {@link Plugin} within this container.
     *
     * @return the plugin dependencies, can be empty
     * @see Plugin#dependencies()
     */
    default Collection<PluginDependency> getDependencies() {
        return ImmutableSet.of();
    }

    default Optional<PluginDependency> getDependency(String id) {
        return Optional.empty();
    }
}