package ufrn.middleware.server.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormDataParser {

    public static Map<String, String> parseFormData(BufferedReader reader) throws IOException {
        Map<String, String> formData = new HashMap<>();
        var stop = false;
        String line;
        while ((line = reader.readLine()) != null || stop) {

            if (line.startsWith("Content-Disposition: form-data")) {
                // Processar a linha de Content-Disposition
                String[] dispositionParts = line.split("; ");
                for (String part : dispositionParts) {
                    if (part.startsWith("name=")) {
                        String paramName = part.substring("name=".length() + 1, part.length() - 1);
                        if (paramName.equals("file")) {
                            stop = true;
                        }
                        line = reader.readLine();
                        String paramValue = line;
                        while (!line.startsWith("-")) {
                            paramValue = paramValue.concat("\n").concat(line);
                            line = reader.readLine();
                        }
                        formData.put(paramName, paramValue);
                    }
                }
            }
        }

        return formData;
    }
}
