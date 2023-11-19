package ufrn.utils;

import ufrn.utils.enums.DefaultHeaders;

import java.util.Map;

public class ResponseEntity<T> {

    int status;
    String message;
    T data;

    Map<String, String> headers;

    public ResponseEntity() {
        headers = DefaultHeaders.JSON.getHeaders();
    }

    public ResponseEntity(int status, String message, T data) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseEntity(int status, String message, T data, DefaultHeaders defaultHeaders) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.headers = defaultHeaders.getHeaders();

    }

    public ResponseEntity(int status, String message, T data, Map<String, String> headers) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.headers = headers;
    }

    public int getStatus() {
        return this.status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJson() {
        return "{" +
                "\"status\":" + status + "," +
                "\"message\":\"" + message + "\"," +
                "\"data\":\"" + data + "\"" +
                "}";
    }

    public String headerString() {
        String res = "";
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            res = res.concat(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        return res;
    }

}


