package ufrn.configuration;

import java.util.Objects;

public enum LifecyclePattern {

    STATIC, PER_REQUEST;

    //Se nao for configurado, será static por padrão
    public static boolean isStaticInstances() {
        var value = MiddlewareProperties.LIFECYCLE_PATTERN.getValue();
        return Objects.isNull(value) || value.equals(LifecyclePattern.STATIC.name());
    }

    public static boolean isPerRequestInstances() {
        return MiddlewareProperties.LIFECYCLE_PATTERN.getValue().equals(LifecyclePattern.PER_REQUEST.name());
    }

    public static LifecyclePattern getLifecyclePattern() {
        var value = MiddlewareProperties.LIFECYCLE_PATTERN.getValue();
        if (Objects.nonNull(value) && !value.isEmpty()) return LifecyclePattern.valueOf(value);
        return LifecyclePattern.STATIC;
    }

}
