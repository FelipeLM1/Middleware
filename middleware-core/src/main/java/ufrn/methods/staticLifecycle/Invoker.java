package ufrn.methods.staticLifecycle;

import ufrn.annotations.http.RequestBody;
import ufrn.annotations.http.RequestParam;
import ufrn.server.HttpRequest;
import ufrn.server.marshaller.MarshallerImpl;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    public static ResponseEntity<?> invoke(HttpRequest requestParam) {

        var httpMethod = requestParam.getHttpMethod();
        var path = requestParam.getPath();
        var jsonString = requestParam.getJsonData();
        var formData = requestParam.getFormData();
        var isRequestParam = false;

        try {
            String[] reqParams = verifyRequestParams(path);
            Map<String, String> params = new HashMap<>();
            if (Objects.nonNull(reqParams)) {
                isRequestParam = true;
                path = reqParams[0];
                params = getRequestParams(reqParams[1]);
            }

            var methodOpt = ObjectIdStatic.getMethod(httpMethod, path);

            Class<?> clazz;
            Object args;
            if ((methodOpt.isPresent())) {
                var method = methodOpt.get();
                clazz = method.getDeclaringClass();
                var obj = clazz.getDeclaredConstructor().newInstance();

                if (httpMethod.equals(HttpMethod.POST) && requestParam.isJsonData()) {
                    args = getRequestBodyParam(method, jsonString);
                    return (ResponseEntity<?>) method.invoke(obj, args);
                } else if (httpMethod.equals(HttpMethod.POST) && requestParam.isFormData()) {
                    args = getRequestBodyFormDataParam(method, formData);
                    return (ResponseEntity<?>) method.invoke(obj, args);
                } else if ((httpMethod.equals(HttpMethod.GET) && isRequestParam)) {
                    Object[] abc = getRequestParamsValues(method, params);
                    return (ResponseEntity<?>) method.invoke(obj, abc);
                } else {
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

    private static Object[] getRequestParamsValues(Method method, Map<String, String> params) {
        return new MarshallerImpl().deserializeRequestParams(method, params);
    }


    private static Map<String, String> getRequestParams(String reqParam) {

        Map<String, String> paramsMap = new HashMap<>();
        String[] params = reqParam.split("&");
        for (String param : params) {
            var strParams = param.split("=");
            paramsMap.put(strParams[0], strParams[1]);
        }

        return paramsMap;
    }

    private static String[] verifyRequestParams(String path) {
        if (path.contains("?")) {
            return path.split("\\?");
        }
        return null;
    }

    private static Object getRequestBodyParam(Method method, String jsonString) {
        Parameter parameter = findAnnotatedParameter(method);
        if (parameter != null) {
            Class<?> parameterType = parameter.getType();
            return new MarshallerImpl().deserialize(jsonString, parameterType);
        }
        return null;
    }

    private static Object getRequestBodyFormDataParam(Method method, Map<String, Object> formData) {
        Parameter parameter = findAnnotatedParameter(method);
        if (parameter != null) {
            Class<?> parameterType = parameter.getType();
            return new MarshallerImpl().deserializeFormData(formData, parameterType);
        }
        return null;
    }

    private static Parameter findAnnotatedParameter(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(RequestBody.class))
                .findFirst()
                .orElse(null);
    }
    //invoke before
    //chamar objeto remoto
    //invoke after -> interceptador

}
