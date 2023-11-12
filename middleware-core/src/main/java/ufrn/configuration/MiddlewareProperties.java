package ufrn.configuration;

public enum MiddlewareProperties {

    TYPE("middleware.type"),
    IP("middleware.ip"),
    PORT("middleware.port"),
    PROTOCOL("middleware.protocol"),
    SCAN("middleware.scan.package"),
    LIFECYCLE_PATTERN("middleware.lifecycle.pattern"),
    ACQUISITION_TYPE("middleware.lifecycle.acquisition-type"),
    LOOKUP_CLIENT_SERVICE_URL("lookup.client.serviceUrl"),
    APPLICATION_NAME("middleware.application.name"),
    APPLICATION_MAIN_CLASS("middleware.application.main");

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
