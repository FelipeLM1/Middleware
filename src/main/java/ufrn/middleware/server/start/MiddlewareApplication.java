package ufrn.middleware.server.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.server.ServerRequestHandler;

public class MiddlewareApplication {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    public static void run() {
        logger.info("Iniciando Middleware...");
        startApplication();
    }

    private static void startApplication() {
        var startTime = System.currentTimeMillis();
        MiddlewareRegisterServices.start();
        ServerRequestHandler.start(startTime);
    }

}
