package ufrn.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.configuration.ApplicationPropertiesReader;
import ufrn.configuration.LifecyclePattern;
import ufrn.configuration.MiddlewareProperties;
import ufrn.server.ClientRequestHandler;
import ufrn.server.ServerRequestHandler;

import java.util.Objects;

/**
 * Main entry point for the Middleware application.
 *
 * <p>The `MiddlewareApplication` class serves as the primary entry point for starting the Middleware
 * application. It initiates the application by initializing essential components and services.
 *
 * @see MiddlewareRegisterServices
 * @see ServerRequestHandler
 */
public class MiddlewareApplication {

    private MiddlewareApplication() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    public static void run() {
        MiddlewareBanner.printBanner();
        logger.info("Iniciando Middleware...");
        if(Objects.equals(MiddlewareProperties.TYPE.getValue(), "SERVER"))
            startApplication();
        else
            connectApplication();
    }

    public static void run(String PathFileConfiguration) {
        MiddlewareBanner.printBanner();
        logger.info("Iniciando Middleware...");
        ApplicationPropertiesReader.setPathFileConfiguration(PathFileConfiguration);
        if(Objects.equals(MiddlewareProperties.TYPE.getValue(), "SERVER"))
            startApplication();
        else
            connectApplication();
    }

    private static void startApplication() {
        var startTime = System.currentTimeMillis();
        if (LifecyclePattern.isStaticInstances()) MiddlewareRegisterServices.start();
        ServerRequestHandler.start(startTime);
    }

    private static void connectApplication() {
        //if (LifecyclePattern.isStaticInstances()) MiddlewareRegisterServices.start();
        ClientRequestHandler.start();
    }

}
