package ufrn.utils.enums;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum DefaultHeaders {

    JSON {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Date", getCurrentDateTime());
            return headers;
        }
    },

    DOWNLOAD {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put(Headers.CONTENT_TYPE.getDescription(), "application/octet-stream");
            headers.put(Headers.DATE.getDescription(), getCurrentDateTime());
            return headers;
        }
    };

    public abstract Map<String, String> getHeaders();

    private static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        return dateFormat.format(new Date());
    }

}
