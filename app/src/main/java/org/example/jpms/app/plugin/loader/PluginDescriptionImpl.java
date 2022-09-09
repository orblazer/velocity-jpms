package org.example.jpms.app.plugin.loader;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.jpms.api.plugin.PluginDescription;
import org.example.jpms.api.plugin.meta.PluginDependency;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class PluginDescriptionImpl implements PluginDescription {
    private final String id;
    private final @Nullable String name;
    private final @Nullable String version;
    private final @Nullable String description;
    private final @Nullable String url;
    private final List<String> authors;
    private final Map<String, PluginDependency> dependencies;

    /**
     * Creates a new plugin description.
     *
     * @param id           the ID
     * @param name         the name of the plugin
     * @param version      the plugin version
     * @param description  a description of the plugin
     * @param url          the website for the plugin
     * @param authors      the authors of this plugin
     * @param dependencies the dependencies for this plugin
     */
    public PluginDescriptionImpl(String id, @Nullable String name, @Nullable String version,
                                 @Nullable String description, @Nullable String url, @Nullable List<String> authors,
                                 Collection<PluginDependency> dependencies) {
        this.id = checkNotNull(id, "id");
        this.name = Strings.emptyToNull(name);
        this.version = Strings.emptyToNull(version);
        this.description = Strings.emptyToNull(description);
        this.url = Strings.emptyToNull(url);
        this.authors = authors == null ? ImmutableList.of() : ImmutableList.copyOf(authors);
        this.dependencies = Maps.uniqueIndex(dependencies, d -> d == null ? null : d.getId());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public Collection<PluginDependency> getDependencies() {
        return dependencies.values();
    }

    @Override
    public Optional<PluginDependency> getDependency(String id) {
        return Optional.ofNullable(dependencies.get(id));
    }

    @Override
    public String toString() {
        return "VelocityPluginDescription{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", version='" + version + '\''
                + ", description='" + description + '\''
                + ", url='" + url + '\''
                + ", authors=" + authors
                + ", dependencies=" + dependencies
                + '}';
    }
}
