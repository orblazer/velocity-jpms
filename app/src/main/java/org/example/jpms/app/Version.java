package org.example.jpms.app;

import java.util.Objects;

/**
 * Provides a version object for the proxy and plugins.
 */
public record Version(String name, String vendor, String version) {
    /**
     * Creates a new {@link Version} instance.
     *
     * @param name    the name for the proxy implementation
     * @param vendor  the vendor for the proxy implementation
     * @param version the version for the proxy implementation
     */
    public Version(String name, String vendor, String version) {
        this.name = Objects.requireNonNull(name, "name");
        this.vendor = Objects.requireNonNull(vendor, "vendor");
        this.version = Objects.requireNonNull(version, "version");
    }
}
