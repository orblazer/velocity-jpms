package org.example.jpms.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jpms.app.plugin.PluginManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private final PluginManager pluginManager;

    private final AtomicBoolean shutdownInProgress = new AtomicBoolean(false);

    App() {
        pluginManager = new PluginManager(this);
    }

    public Version getVersion() {
        Package pkg = App.class.getPackage();
        String implName;
        String implVersion;
        String implVendor;
        if (pkg != null) {
            implName = Optional.ofNullable(pkg.getImplementationTitle()).orElse("JPMS");
            implVersion = Optional.ofNullable(pkg.getImplementationVersion()).orElse("<unknown>");
            implVendor = Optional.ofNullable(pkg.getImplementationVendor()).orElse("JPMS Contributors");
        } else {
            implName = "JPMS";
            implVersion = "<unknown>";
            implVendor = "JPMS Contributors";
        }

        return new Version(implName, implVendor, implVersion);
    }

    void start() {
        logger.info("Booting up {} {}...", getVersion().name(), getVersion().version());

        loadPlugins();
    }

    public void shutdown() {
        if (!shutdownInProgress.compareAndSet(false, true)) {
            return;
        }

        logger.info("Shutting down the proxy...");

        // Since we manually removed the shutdown hook, we need to handle the shutdown ourselves.
        LogManager.shutdown();
    }

    private void loadPlugins() {
        logger.info("Load plugins...");

        try {
            Path pluginPath = Path.of("plugins");

            if (!pluginPath.toFile().exists()) {
                Files.createDirectory(pluginPath);
            } else {
                if (!pluginPath.toFile().isDirectory()) {
                    logger.warn("Plugin location {} is not a directory, continuing without loading plugins",
                            pluginPath);
                    return;
                }

                pluginManager.loadPlugins(pluginPath);
            }
        } catch (Exception e) {
            logger.error("Couldn't load plugins", e);
        }

        logger.info("Loaded {} plugins", pluginManager.getPlugins().size());
    }
}
