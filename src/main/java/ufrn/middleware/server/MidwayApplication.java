package ufrn.middleware.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidwayApplication {

    private static final Logger logger = LoggerFactory.getLogger(MidwayApplication.class);

    public static void start() {
        logger.info("Midway Iniciado!");
        MidwayWebServer.start();
    }


}
