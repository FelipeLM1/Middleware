package ufrn.middleware.server.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.server.ServerRequestHandler;

public class MidwayApplication {

    private static final Logger logger = LoggerFactory.getLogger(MidwayApplication.class);

    public static void run() {
        logger.info("Iniciando Midway...");
        startApplication();
    }

    private static void startApplication() {
        var startTime = System.currentTimeMillis();
        MidwayRegisterServices.start();
        ServerRequestHandler.start(startTime);
    }

}
