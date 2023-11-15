package ufrn.server.http.util;

import ufrn.exceptions.BadRequestException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FormDataParser {

    public static Map<String, Object> parseFormData(BufferedReader reader) throws IOException {
        Map<String, Object> formData = new HashMap<>();

        String contentTypeLine = reader.readLine();

        String boundary = "--------" + contentTypeLine.substring(contentTypeLine.indexOf("boundary=") + 9);

        String line = boundary;
        while (line != null) {
            if (line.equals(boundary)) {
                Map<String, String> headers = new HashMap<>();
                while (line != null && !line.trim().isEmpty()) {
                    String[] headerParts = line.split(": ");
                    if (headerParts.length == 2) {
                        headers.put(headerParts[0].trim(), headerParts[1].trim());
                    }
                    line = reader.readLine();
                }
                // Lê o conteudo
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
//                formData.put(partName, partContent);
                // Check if the part is a file or a regular form field
                if (headers.containsKey("Content-Type")) {
                    // It's a file
                    String fileName = headers.get("Content-Disposition").split(";")[2].trim().split("=")[1].replaceAll("\"", "");
                    File file = saveToFile(partContent, fileName);
                    formData.put(partName, file);
                } else {
                    formData.put(partName, partContent);
                }
            } else {
                break;
            }
        }

        return formData;
    }

    private static File saveToFile(String content, String fileName) {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException exception) {
            throw new BadRequestException("Erro ao ler arquivo" + fileName, 400);
        }
        return file;
    }
}