package ufrn.middleware.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.configuration.AcquisitionType;
import ufrn.middleware.configuration.LifecyclePattern;
import ufrn.middleware.configuration.MiddlewareProperties;
import ufrn.middleware.methods.perRequestLifecycle.ObjectIdPerRequest;
import ufrn.middleware.start.ScannerPerRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

/**
 * A component responsible for handling incoming HTTP requests in the Middleware application.
 *
 * <p>The `ServerRequestHandler` class is a critical component of the Middleware application responsible for
 * processing and handling incoming HTTP requests. It manages the lifecycle of request handling and response
 * generation, making it a central part of the application's functionality.
 */
public class ServerRequestHandler {

    private ServerRequestHandler() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(ServerRequestHandler.class);

    /**
     * Starts the server request handling and processing.
     *
     * @param startTime The timestamp when the server request handling started.
     */
    public static void start(long startTime) {
        logger.info("Iniciando o server...");
        int port = Integer.parseInt(MiddlewareProperties.PORT.getValue());

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            printServerInfo(startTime, port);
            var i = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().name("VThread: " + i)
                        .start(() -> handleRequest(clientSocket, LifecyclePattern.getLifecyclePattern()));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printServerInfo(long startTime, int port) {
        logger.info("Servidor HTTP está rodando na porta {}", port);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Servidor disponivel em {} milissegundos ", duration);
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
        HandleHttpRequest.handleRequest(clientSocket, objectIdPerRequest);
    }
}
