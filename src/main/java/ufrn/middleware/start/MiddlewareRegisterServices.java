package ufrn.middleware.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiddlewareRegisterServices {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);
    public static void start() {
        logger.info("Registrando os servicos...");
        RequestMappingHandlerMapping.init();
        logger.info("servicos registrados com sucesso");
    }
}
