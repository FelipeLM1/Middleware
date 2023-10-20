package ufrn.middleware.server.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.annotations.RequestHttpMapping;
import ufrn.middleware.server.broker.ObjectId;
import ufrn.middleware.server.utils.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class ScannerRequestMethods {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
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

    public static void scanAndAddMethods(String basePackage) {
        findAllClassesUsingClassLoader(basePackage).forEach(ScannerRequestMethods::scanMethodsForAnnotations);
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
            ObjectId.addMethod(httpMethod, value, method);
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
