package ufrn.methods.staticLifecycle;

import ufrn.annotations.http.RequestBody;
import ufrn.server.RequestParam;
import ufrn.server.marshaller.MarshallerJson;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for invoking methods in controllers based on HTTP requests.
 *
 * <p>The {@code Invoker} class is responsible for invoking methods in controllers based
 * on the provided HTTP method and request parameters. It assumes that the controller methods
 * adhere to specific constraints:
 * - Each method should have a single parameter annotated with {@link RequestBody}.
 *
 * <p>Usage:
 * <pre>
 * // Create a request parameter object with the HTTP method and request details
 * RequestParam requestParam = new RequestParam(HttpMethod.POST, "/example", requestBodyJson);
 *
 * // Invoke the method associated with the request
 * Invoker.invoke(requestParam);
 * </pre>
 *
 * @see RequestParam
 * @see RequestBody
 */
public class Invoker {

    private Invoker() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Restrições:
     * Os métodos dos controllers devem ter apenas um atributo com uma anotação @RequestBody.
     **/
    public static ResponseEntity<?> invoke(RequestParam requestParam) {

        var httpMethod = requestParam.httpMethod();
        var path = requestParam.path();
        var jsonString = requestParam.jsonString();

        try {
            var methodOpt = ObjectIdStatic.getMethod(httpMethod, path);
            Class<?> clazz;
            Object[] args;
            if ((methodOpt.isPresent())) {
                var method = methodOpt.get();
                clazz = method.getDeclaringClass();
                var obj = clazz.getDeclaredConstructor().newInstance();

                if (httpMethod.equals(HttpMethod.POST) && Objects.nonNull(jsonString)) {
                    args = getRequestBodyParam(jsonString, method.getParameters());
                    return (ResponseEntity<?>) method.invoke(obj, args);
                } else if (Objects.isNull(jsonString)) {
                    return (ResponseEntity<?>) method.invoke(obj);
                }
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private static Object[] getRequestBodyParam(String jsonString, Parameter[] parameters) {
        return new MarshallerJson().deserialize(jsonString, parameters);
    }


}
