package org.example.jpms.api.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the {@link Plugin} depends on another plugin in order to enable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Dependency {

    /**
     * The plugin ID of the dependency.
     *
     * @return The dependency plugin ID
     */
    String id();

    /**
     * Whether the dependency is not required to enable this plugin. By default, this is
     * {@code false}, meaning that the dependency is required to enable this plugin.
     *
     * @return true if the dependency is not required for the plugin to work
     */
    boolean optional() default false;
}
