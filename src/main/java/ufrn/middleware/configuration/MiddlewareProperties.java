package ufrn.middleware.utils.enums;

import ufrn.middleware.configuration.ApplicationPropertiesReader;

public enum MiddlewareProperties {

    PORT("middleware.port"), SCAN("middleware.scan"),
    LIFECYCLE;

    private String propertyKey;

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
