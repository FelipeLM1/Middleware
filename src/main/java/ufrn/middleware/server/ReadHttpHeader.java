package ufrn.middleware.server;

import java.util.HashMap;
import java.util.Map;

public class ReadHttpHeader {

    static Map<String, String> headers = new HashMap<>();

    public static Map<String, String> getHeaders() {
        return headers;
    }

    public static String getValue(String key) {
        return headers.get(key);
    }

    public static void putValue(String key, String value){
        headers.put(key,value);
    }
}
