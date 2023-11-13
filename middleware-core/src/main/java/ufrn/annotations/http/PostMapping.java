package ufrn.annotations.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated method should handle HTTP POST requests.
 * This annotation should be applied to methods in classes annotated with
 * {@link RequestHttpMapping} to specify that the annotated method is
 * responsible for processing POST requests for a particular URL path.
 *
 * <p>The annotated method may have parameters that correspond to elements
 * of the HTTP request (e.g., request body or form data) and return values
 * that represent the HTTP response.
 *
 * @see RequestHttpMapping
 * @see GetMapping
 */
@RequestHttpMapping
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostMapping {
    String value() default "/";
}
