package ufrn.middleware.utils.enums;

public enum Headers {
    CONTENT_TYPE("Content-Type"), CONTENT_LENGTH("Content-Length");

    private final String description;

    Headers(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
