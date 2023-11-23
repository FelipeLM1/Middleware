package ufrn.configuration;

public enum MiddlewareProperties {

    TYPE("middleware.type"),
    IP("middleware.ip"),
    PORT("middleware.port"),
    PROTOCOL("middleware.protocol"),
    SCAN_PACKAGE("middleware.scan.package"),
    LIFECYCLE_PATTERN("middleware.lifecycle.pattern"),
    ACQUISITION_TYPE("middleware.lifecycle.acquisition-type"),
    LOOKUP_CLIENT_SERVICE_URL("lookup.client.serviceUrl"),
    APPLICATION_NAME("middleware.application.name"),
    APPLICATION_MAIN_CLASS("middleware.application.main"),
    BROKER_HOST("middleware.broker_host"),
    BROKER_PORT("middleware.broker_port"),
    BROKER_USER("middleware.broker_username"),
    BROKER_PASSWORD("middleware.broker_password"),
    APP_PACKAGE_NAME("middleware.app_package_name"),
    MIDDLEWARE_PACKAGE_NAME("middleware.middleware_package_name"),
    PUBLISH_INTERVAL("middleware.publish_interval"),
    IDENTIFICACAO("middleware.app_id");

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
