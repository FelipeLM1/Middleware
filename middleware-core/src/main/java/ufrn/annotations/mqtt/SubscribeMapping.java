package ufrn.annotations.mqtt;

import ufrn.annotations.http.PostMapping;
import ufrn.annotations.http.RequestHttpMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method should be mapped to an HTTP GET request.
 *
 * <p>When a method is annotated with {@code @GetMapping}, it indicates that the
 * method should be invoked when an HTTP GET request is made to the specified
 * URL path. The value of the {@code value} attribute represents the URL path
 * to which the method should respond, and it is set to "/" by default.
 *
 * <p>For example, in a Spring MVC controller, you can use {@code @GetMapping}
 * to handle HTTP GET requests and specify the URL path as an attribute.
 *
 * @see RequestHttpMapping
 * @see PostMapping
 */
@RequestMqttMapping
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeMapping {
    String value() default "/";
}
