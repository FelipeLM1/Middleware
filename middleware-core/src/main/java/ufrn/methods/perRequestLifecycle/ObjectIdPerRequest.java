package ufrn.methods.perRequestLifecycle;

import ufrn.utils.enums.HttpMethod;
import ufrn.utils.print.PrintRegisteredMethods;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectIdPerRequest {

    private final Map<HttpMethod, Map<String, Method>> httpMethodMap;

    public ObjectIdPerRequest() {
        this.httpMethodMap = new EnumMap<>(HttpMethod.class);
        for (HttpMethod method : HttpMethod.values()) {
            httpMethodMap.put(method, new HashMap<>());
        }
    }

    public void addMethod(HttpMethod httpMethod, String path, Method method) {
        this.httpMethodMap.get(httpMethod).put(path, method);
    }

    public Optional<Method> getMethod(HttpMethod httpMethod, String path) {
        if (this.httpMethodMap.containsKey(httpMethod)) {
            var method = this.httpMethodMap.get(httpMethod).get(path);
            return Optional.ofNullable(method);
        }
        return Optional.empty();
    }

    public void printRegisteredMethods() {
        PrintRegisteredMethods.printHttpMethodMap(this.httpMethodMap);
    }
}
