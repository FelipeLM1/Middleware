package ufrn.middleware.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.configuration.LifecyclePattern;
import ufrn.middleware.server.ServerRequestHandler;

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
        startApplication();
    }

    private static void startApplication() {
        var startTime = System.currentTimeMillis();
        if (LifecyclePattern.isStaticInstances()) MiddlewareRegisterServices.start();
        ServerRequestHandler.start(startTime);
    }

}
