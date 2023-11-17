package ufrn.server.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.configuration.AcquisitionType;
import ufrn.configuration.MiddlewareProperties;
import ufrn.methods.perRequestLifecycle.InvokerPerRequest;
import ufrn.methods.perRequestLifecycle.ObjectIdPerRequest;
import ufrn.methods.staticLifecycle.Invoker;
import ufrn.server.RequestParam;
import ufrn.server.http.HandleHttpRequest;
import ufrn.server.http.HttpResponse;
import ufrn.server.http.ReadHttpHeader;
import ufrn.start.ScannerPerRequest;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.ContentType;
import ufrn.utils.enums.Headers;
import ufrn.utils.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;

public class HandleMqttRequest {

    private HandleMqttRequest() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(HandleHttpRequest.class);

    public static void handleRequest(Socket clientSocket, Optional<ObjectIdPerRequest> objectIdPerRequest) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {

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

    private static void callRemoteMethod(Optional<ObjectIdPerRequest> objectIdPerRequest, String method, PrintWriter out, String path, BufferedReader in) throws IOException {
        switch (method) {
            case "PUBLISH" -> handlePublishRequest(out, path, objectIdPerRequest);
            case "SUBSCRIBE" -> handleSubscribeRequest(in, out, path, objectIdPerRequest);
            default -> handleNotFound(out, method);
        }
    }

    //TO-DO
    private static void handlePublishRequest(
            PrintWriter out,
            String path,
            Optional<ObjectIdPerRequest> optionalObjectIdPerRequest) {

        var params = new RequestParam(HttpMethod.GET, path);
        ResponseEntity<?> res;

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

    //TO-DO
    private static void handleSubscribeRequest(
            BufferedReader in,
            PrintWriter out,
            String path, Optional<ObjectIdPerRequest> optionalObjectIdPerRequest)
            throws IOException {

        var headerReader = new ReadHttpHeader();
        headerReader.readHeader(in);

        var contentType = headerReader.getValue(Headers.CONTENT_TYPE.getDescription());
        var contentLengthHeader = Integer.valueOf(headerReader.getValue(Headers.CONTENT_LENGTH.getDescription()));

        if (contentType.startsWith(ContentType.JSON.getDescription())) {
            //handleJsonPost(in, out, path, contentLengthHeader, optionalObjectIdPerRequest);
        } else if (contentType.startsWith(ContentType.MULTIPART_FORM_DATA.getDescription())) {
            //handleFormDataPost(in, out);
        } else {
            out.println("HTTP/1.1 415 Unsupported Media Type");
            out.println();
        }
    }

    private static void handleNotFound(PrintWriter out, String method) {
        var response = "Método não suportado: " + method;
        out.print(response);
        out.flush();
    }

}
