package org.example.jpms.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

public class Bootstrap {
    private static final Logger logger;

    static {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        logger = LogManager.getLogger(Bootstrap.class);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        App app = new App();
        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown,
                "Shutdown thread"));

        double bootTime = (System.currentTimeMillis() - startTime) / 1000d;
        logger.info("Done ({}s)!", new DecimalFormat("#.##").format(bootTime));
    }
}
