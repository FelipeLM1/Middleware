package ufrn.middleware.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service registration class for the Middleware application.
 *
 * <p>The `MiddlewareRegisterServices` class is responsible for registering services and initializing
 * essential components for the Middleware application. It invokes the `RequestMappingHandlerMapping.init()`
 * method to configure the request mapping.
 *
 * @see RequestMappingHandlerMapping
 */
public class MiddlewareRegisterServices {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    public static void start() {
        logger.info("Registrando os servicos...");
        RequestMappingHandlerMapping.init();
        logger.info("servicos registrados com sucesso");
    }
}
