package ufrn.server.http;

import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.Headers;

import java.io.*;
import java.rmi.RemoteException;

public class HttpResponse {

    private HttpResponse() {
        throw new IllegalStateException("Utility class");
    }

    public static void sendJsonResponse(OutputStream out, String jsonResponse, int statusCode) {
        String response = "HTTP/1.1 " + statusCode + "Content-Type: application/json\r\n" + "Content-Length: " + jsonResponse.length() + "\r\n" + "\r\n" + jsonResponse;
        PrintWriter writer = new PrintWriter(out);
        writer.print(response);
        writer.flush();
        writer.close();
    }

    public static void sendJsonResponse(OutputStream out, ResponseEntity<?> response) throws RemoteException {
        var isFile = response.getHeaders().containsKey(Headers.CONTENT_DISPOSITION.getDescription());

        if (!isFile) {
            var resData = response.toJson();
            var resHttp = "HTTP/1.1 " + response.getStatus() + "\r\n" +
                    response.headerString() +
                    Headers.CONTENT_LENGTH.getDescription() + ": " + resData.length() +
                    "\r\n" + "\r\n" + resData;

            PrintWriter writer = new PrintWriter(out);
            writer.print(resHttp);
            writer.flush();
            writer.close();
        } else {
            downloadFile(out, response);
        }
    }

    private static void downloadFile(OutputStream out, ResponseEntity<?> response) throws RemoteException {
        var isFile = response.getData().getClass().equals(File.class);
        if (isFile) {
            File f = (File) response.getData();

            try {
                var resHttp = "HTTP/1.1 " + response.getStatus() + "\r\n" +
                        response.headerString() +
                        Headers.CONTENT_LENGTH.getDescription() + ": " + f.length() +
                        "\r\n" + "\r\n";
                PrintWriter writer = new PrintWriter(out);
                writer.write(resHttp);
                sendFileContent(f.getAbsolutePath(), out);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RemoteException("Erro ao ler bytes do arquivo!");
            }
        } else {
            throw new RemoteException("Não é arquivo!!");
        }
    }

    private static void sendFileContent(String filePath, OutputStream outputStream) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
