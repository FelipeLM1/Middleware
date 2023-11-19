package ufrn.server.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.configuration.AcquisitionType;
import ufrn.configuration.MiddlewareProperties;
import ufrn.methods.perRequestLifecycle.InvokerPerRequest;
import ufrn.methods.perRequestLifecycle.ObjectIdPerRequest;
import ufrn.methods.staticLifecycle.Invoker;
import ufrn.server.RequestParam;
import ufrn.server.http.util.FormDataParser;
import ufrn.start.ScannerPerRequest;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.ContentType;
import ufrn.utils.enums.Headers;
import ufrn.utils.enums.HttpMethod;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HandleHttpRequest {

    private HandleHttpRequest() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(HandleHttpRequest.class);

    public static void handleRequest(Socket clientSocket, Optional<ObjectIdPerRequest> objectIdPerRequest) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            logger.info(requestLine);

            if (requestLine != null) {
                String[] requestParts = requestLine.split("\\s|;");
                String method = requestParts[0];
                String path = requestParts[1];

                if (MiddlewareProperties.ACQUISITION_TYPE.getValue().equals(AcquisitionType.LAZY.name()))
                    new ScannerPerRequest(objectIdPerRequest.orElseThrow(), method, path);

                callRemoteMethod(objectIdPerRequest, method, out, path, in);
            }

        } catch (IOException e) {
            logger.error("Erro de IO: \n {}", e.getMessage());
        }
    }

    private static void callRemoteMethod(Optional<ObjectIdPerRequest> objectIdPerRequest, String method, OutputStream out, String path, BufferedReader in) throws IOException {
        switch (method) {
            case "GET" -> handleGetRequest(out, path, objectIdPerRequest);
            case "POST" -> handlePostRequest(in, out, path, objectIdPerRequest);
            default -> handleNotFound(out);
        }
    }

    private static void handleGetRequest(
            OutputStream out,
            String path,
            Optional<ObjectIdPerRequest> optionalObjectIdPerRequest) throws RemoteException {

        var params = new RequestParam(HttpMethod.GET, path, "");
        ResponseEntity<?> res;

        if (optionalObjectIdPerRequest.isPresent()) {
            var invoker = new InvokerPerRequest(optionalObjectIdPerRequest.get());
            logger.info("Novo Invoker!");
            res = invoker.invoke(params);

        } else {
            logger.info("Invoker Estático!");
            res = Invoker.invoke(params);
        }
        if (Objects.nonNull(res)) HttpResponse.sendJsonResponse(out, res);

    }

    private static void handlePostRequest(
            BufferedReader in,
            OutputStream out,
            String path,
            Optional<ObjectIdPerRequest> optionalObjectIdPerRequest)
            throws IOException {

        var headerReader = new ReadHttpHeader();
        headerReader.readHeader(in);

        var contentType = headerReader.getValue(Headers.CONTENT_TYPE.getDescription());
        var contentLengthHeader = Integer.valueOf(headerReader.getValue(Headers.CONTENT_LENGTH.getDescription()));

        if (contentType.equals(ContentType.JSON.getDescription())) {
            handleJsonPost(in, out, path, contentLengthHeader, optionalObjectIdPerRequest);
        } else if (contentType.equals(ContentType.MULTIPART_FORM_DATA.getDescription())) {
            handleFormDataPost(in, out, path, optionalObjectIdPerRequest);
        } else {
            PrintWriter writer = new PrintWriter(out);
            writer.println("HTTP/1.1 415 Unsupported Media Type");
            writer.println();
        }
    }

    private static void handleFormDataPost(BufferedReader reader, OutputStream writer, String path,
                                           Optional<ObjectIdPerRequest> optionalObjectIdPerRequest
    ) throws IOException {
        Map<String, Object> res = FormDataParser.parseFormData(reader);

        RequestParam params = new RequestParam(HttpMethod.POST, path, res);
        var response = isOptionalObjectIdPresent(optionalObjectIdPerRequest, params);

        sendPostResponse(writer, response);
    }

    private static void handleJsonPost(BufferedReader in, OutputStream out, String path, Integer contentLength,
                                       Optional<ObjectIdPerRequest> optionalObjectIdPerRequest) throws IOException {

        PrintWriter writer = new PrintWriter(out);

        if (contentLength != null) {
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                int bytesRead = in.read(buffer);

                if (bytesRead == contentLength) {
                    String requestBody = new String(buffer);
                    RequestParam params = new RequestParam(HttpMethod.POST, path, requestBody);
                    var res = isOptionalObjectIdPresent(optionalObjectIdPerRequest, params);
                    sendPostResponse(out, res);
                } else {
                    handleContentLengthMismatch(writer);
                }
            } else {
                handleMissingContentLength(writer);
            }
        } else {
            handleMissingContentLengthHeader(writer);
        }
    }

    public static ResponseEntity<?> isOptionalObjectIdPresent(Optional<ObjectIdPerRequest> objectIdPerRequest, RequestParam params) {
        if (objectIdPerRequest.isPresent()) {
            InvokerPerRequest invoker = new InvokerPerRequest(objectIdPerRequest.get());
            logger.info("Novo Invoker!");
            return invoker.invoke(params);
        } else {
            logger.info("Invoker Estático!");
            return Invoker.invoke(params);
        }
    }

    private static void sendPostResponse(OutputStream out, ResponseEntity<?> response) throws RemoteException {
        HttpResponse.sendJsonResponse(out, response);
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

    private static void handleNotFound(OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        var response = "HTTP/1.1 404 Not Found\r\n\r\n";
        response += "Página não encontrada";
        writer.print(response);
        writer.flush();
    }

    private static void handleServerError(PrintWriter out) {
        var response = "HTTP/1.1 500 Internal Server Error\r\n\r\n";
        response += "Ocorreu um erro interno no servidor.";
        out.print(response);
        out.flush();
    }
}
