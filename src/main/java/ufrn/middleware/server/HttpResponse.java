package ufrn.middleware.server;

import java.io.PrintWriter;

public class HttpResponse {

    private HttpResponse() {
        throw new IllegalStateException("Utility class");
    }

    public static void sendJsonResponse(PrintWriter writer, String jsonResponse, int statusCode) {
        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + jsonResponse.length() + "\r\n" +
                "\r\n" +
                jsonResponse;

        writer.print(response);
        writer.flush();
        writer.close();
    }
}
