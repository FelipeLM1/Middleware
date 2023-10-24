package ufrn.middleware.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.configuration.ApplicationPropertiesReader;
import ufrn.middleware.start.MiddlewareApplication;
import ufrn.middleware.utils.enums.MiddlewareProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    public static void start(long startTime) {
        logger.info("Iniciando o server...");
        int port = Integer.parseInt(MiddlewareProperties.PORT.getValue());

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Servidor HTTP está rodando na porta {}", port);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            logger.info("Servidor disponivel em {} milissegundos ", duration);


            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().start(() -> handleRequest(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {
            String requestLine = in.readLine();
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];
            logger.info(requestLine);

            if (method.equals("GET") && path.equals("/")) {
                // Página inicial
                String response = "HTTP/1.1 200 OK\r\n\r\n";
                response += "Hello, World!";
                out.print(response);

            } else if (method.equals("POST") && path.equals("/")) {
                // Lógica para manipular solicitação POST

                // Ler cabeçalhos
                String line;
                int contentLength = -1; // Valor Content-Length inicializado como -1

                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    // Verifique se a linha começa com "Content-Length:"
                    if (line.startsWith("Content-Length: ")) {
                        contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
                    }
                }

                if (contentLength > 0) {
                    // Agora que temos o Content-Length, leia o corpo
                    char[] buffer = new char[contentLength];
                    int bytesRead = in.read(buffer);

                    if (bytesRead == contentLength) {
                        // O buffer agora contém o corpo da solicitação
                        String requestBody = new String(buffer);

                        // Aqui, você pode processar o requestBody conforme necessário
                        System.out.println("Solicitação POST com corpo: " + requestBody);
                    }  // Trate a situação em que a quantidade de bytes lidos não corresponde ao Content-Length

                } else {
                    logger.error("É necessário o contentLength no cabeçalho!");
                }

                // Responda ao cliente
                String response = "HTTP/1.1 200 OK\r\n\r\n";
                response += "Solicitação POST processada com sucesso.";
                out.print(response);
            } else {
                // Página não encontrada para outros métodos ou caminhos
                String response = "HTTP/1.1 404 Not Found\r\n\r\n";
                response += "Página não encontrada";
                out.print(response);
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
