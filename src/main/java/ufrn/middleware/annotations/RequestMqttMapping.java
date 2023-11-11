package ufrn.middleware.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that an annotated class is capable of handling HTTP requests.
 * Classes annotated with {@code @RequestHttpMapping} can contain methods
 * annotated with HTTP-specific annotations like {@code @GetMapping},
 * {@code @PostMapping}, and so on.
 *
 * <p>HTTP request handling methods within classes annotated with
 * {@code @RequestHttpMapping} are responsible for processing requests
 * for specific URL paths.
 *
 * @see GetMapping
 * @see PostMapping
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMqttMapping {
}
