package ufrn.utils;

import com.google.gson.Gson;

public record ResponseEntity<T>(int status, String message, T data) {
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
