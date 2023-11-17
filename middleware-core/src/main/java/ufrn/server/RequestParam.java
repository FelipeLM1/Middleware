package ufrn.server;

import ufrn.utils.enums.HttpMethod;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a request parameter containing HTTP method, path, and JSON string data.
 *
 * <p>The `RequestParam` class is a record that encapsulates information about a request parameter,
 * including the HTTP method, path, and JSON string data. It is used to pass this information
 * between components of a remote communication system.
 *
 * @see HttpMethod
 */
public class RequestParam {
    HttpMethod httpMethod;
    String path;
    String jsonData;
    Map<String, Object> formData;
    public RequestParam() {
    }

    public RequestParam(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public RequestParam(HttpMethod httpMethod, String path, String jsonData) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.jsonData = jsonData;
    }

    public RequestParam(HttpMethod httpMethod, String path, Map<String, Object> formData) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.formData = formData;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

    public boolean isFormData() {
        if (Objects.nonNull(this.formData) && jsonData == null) {
            return !this.formData.isEmpty();
        }
        return false;
    }

    public boolean isJsonData() {
        return !isFormData() && !jsonData.isBlank();
    }
}
