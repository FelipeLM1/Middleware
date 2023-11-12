package ufrn.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.configuration.AcquisitionType;
import ufrn.configuration.LifecyclePattern;
import ufrn.configuration.MiddlewareProperties;
import ufrn.methods.perRequestLifecycle.ObjectIdPerRequest;
import ufrn.server.mqtt.HandleMqttRequest;
import ufrn.start.ScannerPerRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

/**
 * A component responsible for handling incoming HTTP requests in the Middleware application.
 *
 * <p>The `ServerRequestHandler` class is a critical component of the Middleware application responsible for
 * processing and handling incoming HTTP requests. It manages the lifecycle of request handling and response
 * generation, making it a central part of the application's functionality.
 */
public class ClientRequestHandler {

    private ClientRequestHandler() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);

    /**
     * Starts the client request handling and processing.
     *
     */
    public static void start() {
        int port = Integer.parseInt(MiddlewareProperties.PORT.getValue());
        String ip = MiddlewareProperties.IP.getValue();

        logger.info("Conectando ao servidor " + ip + ":" + port + "...");

        try  (Socket socket = new Socket(ip, port)) {
            Thread.ofVirtual().name("VThread: 1").start(() -> handleRequest(socket, LifecyclePattern.getLifecyclePattern()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printClientInfo(int port) {
        logger.info("Cliente conectado a porta {}", port);
    }

    private static void handleRequest(Socket clientSocket, LifecyclePattern lifecyclePattern) {
        Optional<ObjectIdPerRequest> objectIdPerRequest = Optional.empty();

        var acquisitionType = AcquisitionType.valueOf(MiddlewareProperties.ACQUISITION_TYPE.getValue());

        if (LifecyclePattern.PER_REQUEST.equals(lifecyclePattern)) {
            objectIdPerRequest = Optional.of(new ObjectIdPerRequest());
            if (acquisitionType.equals(AcquisitionType.EAGER)) {
                new ScannerPerRequest(objectIdPerRequest.get());
            }
        }
        //var reqHandler = new RequestHandler(clientSocket, objectIdPerRequest);
        //reqHandler.handleRequest();
        HandleMqttRequest.handleRequest(clientSocket, objectIdPerRequest);
    }

}