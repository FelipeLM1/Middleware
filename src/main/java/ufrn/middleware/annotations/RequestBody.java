package ufrn.middleware.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method parameter should be bound to the body of an HTTP request.
 * The parameter annotated with {@code @RequestBody} is used to extract the content
 * of the HTTP request body and convert it into an object of the specified type.
 *
 * <p>When a method is annotated with {@code @PostMapping} or a similar HTTP-specific
 * annotation, you can use {@code @RequestBody} to indicate that the method parameter
 * should be populated with the content of the HTTP request body.
 *
 * <p>For example, in a Spring MVC controller method, you can use {@code @RequestBody}
 * to receive a JSON or XML payload as an object, which is automatically deserialized.
 *
 * @see PostMapping
 * @see GetMapping
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestBody {


}
