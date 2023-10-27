package ufrn.middleware.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.configuration.LifecyclePattern;
import ufrn.middleware.configuration.MiddlewareProperties;
import ufrn.middleware.methods.Invoker;
import ufrn.middleware.methods.InvokerPerRequest;
import ufrn.middleware.methods.ObjectIdPerRequest;
import ufrn.middleware.start.ScannerPerRequest;
import ufrn.middleware.utils.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            logger.info("Servidor HTTP está rodando na porta {}", port);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            logger.info("Servidor disponivel em {} milissegundos ", duration);

            var i = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().name("VThread: " + i).start(() -> handleRequest(clientSocket, LifecyclePattern.getLifecyclePattern()));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket, LifecyclePattern lifecyclePattern) {
        Optional<ObjectIdPerRequest> objectIdPerRequest = Optional.empty();

        if (LifecyclePattern.PER_REQUEST.equals(lifecyclePattern)) {
            objectIdPerRequest = Optional.of(new ObjectIdPerRequest());
            var scanner = new ScannerPerRequest(objectIdPerRequest.get());
            scanner.scanAndAddMethods();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            logger.info(requestLine);

            if (requestLine != null) {
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                if ("GET".equals(method)) {
                    handleGetRequest(out, path, objectIdPerRequest);
                } else if ("POST".equals(method)) {
                    handlePostRequest(in, out, path, objectIdPerRequest);
                } else {
                    handleNotFound(out);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleGetRequest(
            PrintWriter out,
            String path,
            Optional<ObjectIdPerRequest> optionalObjectIdPerRequest) {
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        response += "Hello, World!";
        var params = new RequestParam(HttpMethod.GET, path, null);


        if (optionalObjectIdPerRequest.isPresent()) {
            var invoker = new InvokerPerRequest(optionalObjectIdPerRequest.get());
            logger.info("Novo Invoker!");
            invoker.invoke(params);

        } else {
            logger.info("Invoker Estático!");
            Invoker.invoke(params);
        }
        out.print(response);
        out.flush();
    }

    private static void handlePostRequest(
            BufferedReader in,
            PrintWriter out,
            String path, Optional<ObjectIdPerRequest> optionalObjectIdPerRequest)
            throws IOException {

        String contentLengthHeader = readContentLengthHeader(in);
        int contentLength;

        while (true) {
            var line = in.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
        }


        if (contentLengthHeader != null) {
            contentLength = Integer.parseInt(contentLengthHeader);

            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                int bytesRead = in.read(buffer);

                if (bytesRead == contentLength) {
                    String requestBody = new String(buffer);
                    var params = new RequestParam(HttpMethod.POST, path, requestBody);
                    if (optionalObjectIdPerRequest.isPresent()) {
                        var invoker = new InvokerPerRequest(optionalObjectIdPerRequest.get());
                        logger.info("Novo Invoker!");
                        invoker.invoke(params);

                    } else {
                        logger.info("Invoker Estático!");
                        Invoker.invoke(params);
                    }

                    sendPostResponse(out);
                } else {
                    handleContentLengthMismatch(out);
                }
            } else {
                handleMissingContentLength(out);
            }
        } else {
            handleMissingContentLengthHeader(out);
        }
    }

    private static String readContentLengthHeader(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length: ")) {
                return line.substring("Content-Length: ".length());
            }
        }
        return null;
    }

    private static void sendPostResponse(PrintWriter out) {
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        response += "Solicitação POST processada com sucesso.";
        out.print(response);
        out.flush();
    }

    private static void handleContentLengthMismatch(PrintWriter out) {
        logger.error("A quantidade de bytes lidos não corresponde ao Content-Length.");
        handleServerError(out);
    }

    private static void handleMissingContentLength(PrintWriter out) {
        logger.error("É necessário o Content-Length no cabeçalho.");
        handleServerError(out);
    }

    private static void handleMissingContentLengthHeader(PrintWriter out) {
        logger.error("Cabeçalho Content-Length ausente.");
        handleServerError(out);
    }

    private static void handleNotFound(PrintWriter out) {
        String response = "HTTP/1.1 404 Not Found\r\n\r\n";
        response += "Página não encontrada";
        out.print(response);
        out.flush();
    }

    private static void handleServerError(PrintWriter out) {
        String response = "HTTP/1.1 500 Internal Server Error\r\n\r\n";
        response += "Ocorreu um erro interno no servidor.";
        out.print(response);
        out.flush();
    }
}
