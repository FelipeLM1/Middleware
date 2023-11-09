package ufrn.middleware.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.configuration.AcquisitionType;
import ufrn.middleware.configuration.MiddlewareProperties;
import ufrn.middleware.methods.perRequestLifecycle.InvokerPerRequest;
import ufrn.middleware.methods.perRequestLifecycle.ObjectIdPerRequest;
import ufrn.middleware.methods.staticLifecycle.Invoker;
import ufrn.middleware.start.ScannerPerRequest;
import ufrn.middleware.utils.ResponseEntity;
import ufrn.middleware.utils.enums.Headers;
import ufrn.middleware.utils.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;

public class HandleHttpRequest {

    private HandleHttpRequest() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(HandleHttpRequest.class);

    public static void handleRequest(Socket clientSocket, Optional<ObjectIdPerRequest> objectIdPerRequest) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            logger.info(requestLine);

            if (requestLine != null) {
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                if (MiddlewareProperties.ACQUISITION_TYPE.getValue().equals(AcquisitionType.LAZY.name()))
                    new ScannerPerRequest(objectIdPerRequest.orElseThrow(), method, path);

                callRemoteMethod(objectIdPerRequest, method, out, path, in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void callRemoteMethod(Optional<ObjectIdPerRequest> objectIdPerRequest, String method, PrintWriter out, String path, BufferedReader in) throws IOException {
        if ("GET".equals(method)) {
            handleGetRequest(out, path, objectIdPerRequest);
        } else if ("POST".equals(method)) {
            handlePostRequest(in, out, path, objectIdPerRequest);
        } else {
            handleNotFound(out);
        }
    }


    private static void handleGetRequest(
            PrintWriter out,
            String path,
            Optional<ObjectIdPerRequest> optionalObjectIdPerRequest) {

        var params = new RequestParam(HttpMethod.GET, path, null);
        ResponseEntity<?> res = null;

        if (optionalObjectIdPerRequest.isPresent()) {
            var invoker = new InvokerPerRequest(optionalObjectIdPerRequest.get());
            logger.info("Novo Invoker!");
            res = invoker.invoke(params);

        } else {
            logger.info("Invoker Estático!");
            res = Invoker.invoke(params);
        }
        if (Objects.nonNull(res)) HttpResponse.sendJsonResponse(out, res.toJson(), res.status());

    }

    private static void handlePostRequest(
            BufferedReader in,
            PrintWriter out,
            String path, Optional<ObjectIdPerRequest> optionalObjectIdPerRequest)
            throws IOException {

        readHeader(in);
        String contentLengthHeader = ReadHttpHeader.getValue("Content-Length");
        String contentType = ReadHttpHeader.getValue("Content-Type");

        System.out.println("CONTENTLENGHT");
        System.out.println(contentLengthHeader);
        System.out.println("CONTENTTYPE");
        System.out.println(contentType);
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
                    System.out.println("CONTENT-HEADER");
                    System.out.println(contentLengthHeader);

                    handleContentLengthMismatch(out);
                }
            } else {
                handleMissingContentLength(out);
            }
        } else {
            handleMissingContentLengthHeader(out);
        }
    }

    private static void readHeader(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && !line.isBlank()) {
            if (line.startsWith("Content-Length: ")) {
                ReadHttpHeader.putValue(Headers.CONTENT_LENGTH.getDescription(), line.substring("Content-Length: ".length()));
            }
            if (line.startsWith("Content-Type: ")) {
                ReadHttpHeader.putValue(Headers.CONTENT_TYPE.getDescription(), line.substring("Content-Type:".length()));
            }
        }
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
