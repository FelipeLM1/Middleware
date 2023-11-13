package ufrn;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void start() {

        int portNumber = 8761;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            logger.info("Servidor aguardando conexões na porta: {}", portNumber);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    // Lê a mensagem do cliente
                    String mensagemDoCliente = in.readLine();
                    logger.info("Mensagem do cliente: {}", mensagemDoCliente);
                    addService(new Gson().fromJson(mensagemDoCliente, ServiceDto.class));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addService(ServiceDto service) {
        if (service.config().equals("HTTP")) {
            ServiceRegistry.addHttpService(service);
        } else if (service.config().equals("MQTT")) {
            ServiceRegistry.addMqttService(service);
        }
    }
}

