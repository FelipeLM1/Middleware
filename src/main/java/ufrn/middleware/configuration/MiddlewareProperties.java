package ufrn.middleware.configuration;

public enum MiddlewareProperties {

    TYPE("middleware.type"),
    IP("middleware.ip"),
    PORT("middleware.port"),
    PROTOCOL("middleware.protocol"),
    SCAN("middleware.scan.package"),
    LIFECYCLE_PATTERN("middleware.lifecycle.pattern"),
    ACQUISITION_TYPE("middleware.lifecycle.acquisition-type");

    private final String propertyKey;

    MiddlewareProperties(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return this.propertyKey;
    }

    public String getValue() {
        return ApplicationPropertiesReader.getProperty(this.propertyKey);
    }

}
