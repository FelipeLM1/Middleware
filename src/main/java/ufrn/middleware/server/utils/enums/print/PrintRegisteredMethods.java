package ufrn.middleware.server.utils.enums.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.server.start.MiddlewareApplication;
import ufrn.middleware.server.utils.enums.HttpMethod;

import java.lang.reflect.Method;
import java.util.Map;

public class PrintRegisteredMethods {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    public static void printHttpMethodMap(Map<HttpMethod, Map<String, Method>> httpMethodMap) {
        for (Map.Entry<HttpMethod, Map<String, Method>> entry : httpMethodMap.entrySet()) {
            HttpMethod httpMethod = entry.getKey();
            Map<String, Method> methodMap = entry.getValue();

            logger.info("HTTP Method: {}", httpMethod);
            logger.info("Mapped Paths:");

            for (Map.Entry<String, Method> pathEntry : methodMap.entrySet()) {
                String path = pathEntry.getKey();
                Method method = pathEntry.getValue();
                logger.info("    Path: {}", path);
                logger.info("    Method: {}", method.getName());
                logger.info("");
            }
        }
    }

}
