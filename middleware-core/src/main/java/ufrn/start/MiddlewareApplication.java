package ufrn.start;

import java.io.IOException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ufrn.broker.BrokerRequestHandler;
import ufrn.client.AppStarter;
import ufrn.configuration.ApplicationPropertiesReader;
import ufrn.configuration.LifecyclePattern;
import ufrn.configuration.MiddlewareProperties;
import ufrn.server.ServerRequestHandler;

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
        
        if(Objects.equals(MiddlewareProperties.TYPE.getValue(), "SERVER")) {
        	MiddlewareBanner.printBanner();
        	logger.info("Iniciando Middleware...");
            startApplication();
        
        } else if(Objects.equals(MiddlewareProperties.PROTOCOL.getValue(), "MQTT") && 
        		Objects.equals(MiddlewareProperties.TYPE.getValue(), "BROKER")) {
            startBroker();
        
        }else if(Objects.equals(MiddlewareProperties.PROTOCOL.getValue(), "MQTT") &&
        		Objects.equals(MiddlewareProperties.TYPE.getValue(), "CLIENT"))
            startMQTTClient();
    }

	public static void run(String PathFileConfiguration) {
        MiddlewareBanner.printBanner();
        logger.info("Iniciando Middleware...");
        ApplicationPropertiesReader.setPathFileConfiguration(PathFileConfiguration);
        
        if(Objects.equals(MiddlewareProperties.TYPE.getValue(), "SERVER"))
            startApplication();
    }

    private static void startApplication() {
        var startTime = System.currentTimeMillis();
        if (LifecyclePattern.isStaticInstances()) MiddlewareRegisterServices.start();
        ServerRequestHandler.start(startTime);
    }

    private static void startMQTTClient() {
		AppStarter.start();
	}

	private static void startBroker() {
		try {
			BrokerRequestHandler.iniciarServidorMQTT();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
