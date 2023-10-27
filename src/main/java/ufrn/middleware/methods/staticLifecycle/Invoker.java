package ufrn.middleware.methods.staticLifecycle;

import ufrn.middleware.annotations.RequestBody;
import ufrn.middleware.server.MarshallerImpl;
import ufrn.middleware.server.RequestParam;
import ufrn.middleware.utils.enums.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
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
    public static void invoke(RequestParam requestParam) {

        var httpMethod = requestParam.httpMethod();
        var path = requestParam.path();
        var jsonString = requestParam.jsonString();

        try {
            var methodOpt = ObjectIdStatic.getMethod(httpMethod, path);
            Class<?> clazz;
            Object args;
            if ((methodOpt.isPresent())) {
                var method = methodOpt.get();
                clazz = method.getDeclaringClass();
                var obj = clazz.getDeclaredConstructor().newInstance();

                if (httpMethod.equals(HttpMethod.POST) && Objects.nonNull(jsonString)) {
                    args = getRequestBodyParam(method, jsonString);
                    method.invoke(obj, args);
                } else if (Objects.isNull(jsonString)) {
                    method.invoke(obj);
                }

            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private static Object getRequestBodyParam(Method method, String jsonString) {
        Parameter parameter = findAnnotatedParameter(method, RequestBody.class);
        if (parameter != null) {
            Class<?> parameterType = parameter.getType();
            return new MarshallerImpl().deserialize(jsonString, parameterType);
        }
        return null;
    }

    private static Parameter findAnnotatedParameter(Method method, Class<? extends Annotation> annotationType) {
        return Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(annotationType))
                .findFirst()
                .orElse(null);
    }
    //invoke before
    // chamar objeto remoto
    //invoke after -> interceptador

}
