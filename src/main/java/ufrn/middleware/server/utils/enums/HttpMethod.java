package ufrn.middleware.server.utils.enums;

public enum HttpMethod {
    GET, POST;

    public static HttpMethod fromString(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }
        throw new IllegalArgumentException("Método HTTP não suportado: " + method);
    }
}
