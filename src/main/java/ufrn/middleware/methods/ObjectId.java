package ufrn.middleware.methods;

import ufrn.middleware.utils.enums.HttpMethod;
import ufrn.middleware.utils.print.PrintRegisteredMethods;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


//OBJECT ID
public class ObjectId {

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

    public static Method getMethod(HttpMethod httpMethod, String path) {
        if (httpMethodMap.containsKey(httpMethod)) {
            return httpMethodMap.get(httpMethod).get(path);
        }
        return null;
    }

    public static void printRegisteredMethods() {
        PrintRegisteredMethods.printHttpMethodMap(httpMethodMap);
    }
}