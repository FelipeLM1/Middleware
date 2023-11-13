package ufrn.server.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormDataParser {

    public static Map<String, String> parseFormData(BufferedReader reader) throws IOException {
        Map<String, String> formData = new HashMap<>();
        // Read the content type line
        String contentTypeLine = reader.readLine();

        // Read the boundary from the content type line
        String boundary = "--------" + contentTypeLine.substring(contentTypeLine.indexOf("boundary=") + 9);

        // Process each part in the multipart content
        String line = boundary;
        while (line != null) {
            if (line.equals(boundary)) {
                // Read headers for the part
//                line = reader.readLine();
                Map<String, String> headers = new HashMap<>();
                while (line != null && !line.trim().isEmpty()) {
                    String[] headerParts = line.split(": ");
                    if (headerParts.length == 2) {
                        headers.put(headerParts[0].trim(), headerParts[1].trim());
                    }
                    line = reader.readLine();
                    System.out.println("i>> " + line);
                }
//                return formData;
//              Read the part content
                StringBuilder content = new StringBuilder();
                line = reader.readLine();
                while (line != null && !line.contains(boundary)) {
                    content.append(line);
                    content.append("\r\n");
                    line = reader.readLine();
                }
                // Extract the part name and value
                String partContent = content.toString().trim();
                String partName = headers.get("Content-Disposition").split(";")[1].trim().split("=")[1].replaceAll("\"", "");
                formData.put(partName, partContent);
//                return formData;
            } else {
                System.out.println("e>> " + line);
                break;
            }
        }

        return formData;
    }
}