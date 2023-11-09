package ufrn.middleware.utils.enums;

public enum ContentType {

    JSON("application/json"), MULTIPART_FORM_DATA("multipart/form-data");

    final String description;

    ContentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
