module org.example.jpms {
    requires org.example.jpms.api;

    // Compilation libraries
    requires org.checkerframework.checker.qual;

    // Libraries
    requires org.apache.logging.log4j;
    requires com.google.common;
    requires com.google.guice;
    requires org.slf4j;
}