package ufrn.middleware.methods.staticLifecycle;

import ufrn.middleware.utils.enums.HttpMethod;
import ufrn.middleware.utils.print.PrintRegisteredMethods;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Utility class for mapping HTTP methods to request paths and associated methods.
 *
 * <p>The {@code ObjectId} class is used to map HTTP methods (such as GET and POST)
 * to request paths and the corresponding Java methods. It provides a data structure
 * that allows efficient lookup and dispatch of methods based on HTTP method and request path.
 *
 * <p>Usage:
 * <pre>
 * // Retrieve a method associated with a specific HTTP method and path
 * Method method = ObjectId.getMethod(HttpMethod.GET, "/example");
 * </pre>
 *
 * @see HttpMethod
 * @see Method
 */
public class ObjectIdStatic {

    private static final Map<HttpMethod, Map<String, Method>> httpMethodMap;

    static {
        httpMethodMap = new EnumMap<>(HttpMethod.class);
        for (HttpMethod method : HttpMethod.values()) {
            httpMethodMap.put(method, new HashMap<>());
        }
    }

    public static void addMethod(HttpMethod httpMethod, String path, Method method) {
        httpMethodMap.get(httpMethod).put(path, method);
    }

    public static Optional<Method> getMethod(HttpMethod httpMethod, String path) {
        if (httpMethodMap.containsKey(httpMethod)) {
            return Optional.of(httpMethodMap.get(httpMethod).get(path));
        }
        return Optional.empty();
    }

    public static void printRegisteredMethods() {
        PrintRegisteredMethods.printHttpMethodMap(httpMethodMap);
    }
}
