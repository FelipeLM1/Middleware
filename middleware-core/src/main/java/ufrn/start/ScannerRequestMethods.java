package ufrn.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.annotations.GetMapping;
import ufrn.annotations.PostMapping;
import ufrn.annotations.RequestHttpMapping;
import ufrn.methods.staticLifecycle.ObjectIdStatic;
import ufrn.utils.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A utility class for scanning and registering request handling methods in the specified package.
 *
 * <p>The `ScannerRequestMethods` class provides utility methods for scanning classes in a specified package,
 * searching for request handling methods, and registering them for the Middleware application. It is used to
 * locate and register methods annotated with request mapping annotations such as `GetMapping` and `PostMapping`.
 *
 * @see GetMapping
 * @see PostMapping
 */
public class ScannerRequestMethods {

    private ScannerRequestMethods() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(ScannerRequestMethods.class);

    public static void scanAndAddMethods(String basePackage) {
        findAllClassesUsingClassLoader(basePackage).forEach(ScannerRequestMethods::scanMethodsForAnnotations);
    }


    private static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return null;
    }


    private static void scanMethodsForAnnotations(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(RequestHttpMapping.class)) {
                    HttpMethod httpMethod = mapAnnotationToHttpMethod(annotation);
                    addRequestMethod(httpMethod, annotation, method);
                }
            }
        }
    }

    private static void addRequestMethod(HttpMethod httpMethod, Annotation annotation, Method method) {
        if (annotation instanceof GetMapping || annotation instanceof PostMapping) {
            String value = getMappingValue(annotation);
            ObjectIdStatic.addMethod(httpMethod, value, method);
        }
    }

    private static String getMappingValue(Annotation annotation) {
        if (annotation instanceof GetMapping getMapping) {
            return getMapping.value();
        } else if (annotation instanceof PostMapping postMapping) {
            return postMapping.value();
        }
        return "";
    }

    private static HttpMethod mapAnnotationToHttpMethod(Annotation annotation) {
        return switch (annotation.annotationType().getSimpleName()) {
            case "GetMapping" -> HttpMethod.GET;
            case "PostMapping" -> HttpMethod.POST;
            default -> null;
        };
    }
}
