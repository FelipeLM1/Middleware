package ufrn.middleware.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.annotations.RequestHttpMapping;
import ufrn.middleware.configuration.MiddlewareProperties;
import ufrn.middleware.methods.perRequestLifecycle.ObjectIdPerRequest;
import ufrn.middleware.utils.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class ScannerPerRequest {
    private final Logger logger = LoggerFactory.getLogger(ScannerPerRequest.class);

    private final ObjectIdPerRequest objectIdPerRequest;


    public ScannerPerRequest(ObjectIdPerRequest objectIdPerRequest) {
        logger.info("Scanner Per Request Eager iniciando...");
        this.objectIdPerRequest = objectIdPerRequest;
        this.scanAndAddAllMethods();
    }

    public ScannerPerRequest(ObjectIdPerRequest objectIdPerRequest, String reqMethod, String pathLazyMethod) {
        logger.info("Scanner Per Request Lazy iniciando...");
        this.objectIdPerRequest = objectIdPerRequest;
        this.scanAndAddLazyMethod(reqMethod, pathLazyMethod);

    }

    private void scanAndAddLazyMethod(String httpMethodReq, String pathLazyMethod) {
        this.findAllClassesUsingClassLoader(MiddlewareProperties.SCAN.getValue())
                .forEach(aClass -> scanLazyMethodForAnnotations(aClass, httpMethodReq, pathLazyMethod));
    }

    public void scanAndAddAllMethods() {
        this.findAllClassesUsingClassLoader(MiddlewareProperties.SCAN.getValue()).forEach(this::scanMethodsForAnnotations);
    }

    private Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private void scanMethodsForAnnotations(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(RequestHttpMapping.class)) {
                    HttpMethod httpMethod = mapAnnotationToHttpMethod(annotation);
                    this.addRequestMethod(httpMethod, annotation, method);
                }
            }
        }
    }

    private void addRequestMethod(HttpMethod httpMethod, Annotation annotation, Method method) {
        if (annotation instanceof GetMapping || annotation instanceof PostMapping) {
            String value = getMappingValue(annotation);
            this.objectIdPerRequest.addMethod(httpMethod, value, method);
            logger.info("método per request adicionado: {}", method.getName());
        }
    }

    private void scanLazyMethodForAnnotations(Class<?> clazz, String httpMethodReq, String path) {
        for (Method method : clazz.getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(RequestHttpMapping.class)) {
                    HttpMethod httpMethod = mapAnnotationToHttpMethod(annotation);
                    if (httpMethodReq.equals(httpMethod.name())) {
                        var findMethod = this.addLazyRequestMethod(httpMethod, annotation, method, path);
                        if (findMethod) return;
                    }
                }
            }
        }
    }

    private boolean addLazyRequestMethod(HttpMethod httpMethod, Annotation annotation, Method method, String path) {
        if (annotation instanceof GetMapping || annotation instanceof PostMapping) {
            String value = getMappingValue(annotation);
            if (value.equals(path)) {
                this.objectIdPerRequest.addMethod(httpMethod, value, method);
                logger.info("método Lazy per request adicionado: {}", method.getName());
                return true;
            }
        }
        return false;
    }

    private String getMappingValue(Annotation annotation) {
        if (annotation instanceof GetMapping getMapping) {
            return getMapping.value();
        } else if (annotation instanceof PostMapping postMapping) {
            return postMapping.value();
        }
        return "";
    }

    private HttpMethod mapAnnotationToHttpMethod(Annotation annotation) {
        return switch (annotation.annotationType().getSimpleName()) {
            case "GetMapping" -> HttpMethod.GET;
            case "PostMapping" -> HttpMethod.POST;
            default -> null;
        };
    }
}
