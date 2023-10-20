package ufrn.middleware.server.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidwayRegisterServices {

    private static final Logger logger = LoggerFactory.getLogger(MidwayApplication.class);
    public static void start() {
        logger.info("Registrando os servicos...");
        RequestMappingHandlerMapping.init();
        logger.info("servicos registrados com sucesso");
    }
}
