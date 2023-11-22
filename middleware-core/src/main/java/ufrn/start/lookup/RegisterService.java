package ufrn.start.lookup;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.annotations.config.EnableLookupServiceDiscovery;
import ufrn.configuration.MiddlewareProperties;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class RegisterService {

    private RegisterService() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);


    public static void start() {
        var enableRegisterService = enableLookupServiceDiscovery();
        if (enableRegisterService) sendRequest();
    }

    public static void sendRequest() {
        var urlStr = MiddlewareProperties.LOOKUP_CLIENT_SERVICE_URL.getValue();
        URL url;
        try {
            url = URI.create(urlStr).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try (Socket socket = new Socket(url.getHost(), url.getPort());
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            String req = getMessageReq();

            out.println(req);
            logger.info("Mensagem para o servidor enviada: {}", req);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMessageReq() {
        var appName = MiddlewareProperties.APPLICATION_NAME.getValue();
        var msg = new ServiceDto(appName, "localhost:8080", MiddlewareProperties.PROTOCOL.getValue());
        return new Gson().toJson(msg);
    }

    private static boolean enableLookupServiceDiscovery() {
        var className = MiddlewareProperties.APPLICATION_MAIN_CLASS.getValue();
        try {
            if(Objects.nonNull(className)) {
                Class<?> clazz = Class.forName(className);
                return clazz.isAnnotationPresent(EnableLookupServiceDiscovery.class);
            }else{
                logger.info("Lookup está desativado!");
            }
        } catch (ClassNotFoundException e) {
            logger.error("Classe não encontrada: {}", className);
        }
        return false;
    }

}
