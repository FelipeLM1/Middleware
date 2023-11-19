package ufrn.utils.enums;

public enum Headers {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    DATE("Date"),
    CONTENT_DISPOSITION("Content-Disposition");

    private final String description;

    Headers(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
