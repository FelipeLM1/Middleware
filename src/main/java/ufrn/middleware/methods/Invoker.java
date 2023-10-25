package ufrn.middleware.methods;

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

//chama o objectID
public class Invoker {

    /**
     * Restrições:
     * Os métodos dos controllers devem ter apenas um atributo com uma anotação @RequestBody.
     **/
    public static void invoke(HttpMethod httpMethod, RequestParam requestParam) {

        var path = requestParam.path();
        var jsonString = requestParam.jsonString();

        try {
            var methodOpt = ObjectId.getMethod(httpMethod, path);
            Class<?> clazz;
            Object args = null;
            if ((methodOpt.isPresent())) {
                var method = methodOpt.get();
                clazz = method.getDeclaringClass();
                var obj = clazz.getDeclaredConstructor().newInstance();

                if (httpMethod.equals(HttpMethod.POST) && Objects.nonNull(jsonString)) {
                    args = getRequestBodyParam(method, jsonString);
                }

                method.invoke(obj, args);
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
