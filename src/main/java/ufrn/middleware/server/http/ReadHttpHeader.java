package ufrn.middleware.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadHttpHeader {

    private final Map<String, String> headers = new HashMap<>();

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void readHeader(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && !line.isBlank()) {
            var strings = line.split("\\s*:\\s*|;");
            String key = strings[0];
            String value = strings[1];
            this.putValue(key, value);
        }
    }

    public String getValue(String key) {
        return headers.get(key);
    }

    public void putValue(String key, String value) {
        this.headers.put(key, value);
    }
}
