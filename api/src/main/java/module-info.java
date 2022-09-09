/**
 * The api for jpms example
 */
module org.example.jpms.api {
    // Compilation libraries
    requires org.checkerframework.checker.qual;

    // Libraries
    requires com.google.common;
    requires com.google.guice;

    // Exports packages
    exports org.example.jpms.api;
    exports org.example.jpms.api.plugin;
    exports org.example.jpms.api.plugin.meta;
    exports org.example.jpms.api.plugin.annotation;
}