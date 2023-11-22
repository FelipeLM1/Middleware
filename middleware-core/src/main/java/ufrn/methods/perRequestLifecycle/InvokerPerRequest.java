package ufrn.methods.perRequestLifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.annotations.http.RequestBody;
import ufrn.server.marshaller.MarshallerImpl;
import ufrn.server.HttpRequest;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

public class InvokerPerRequest {
    /**
     * Restrições:
     * Os métodos dos controllers devem ter apenas um atributo com uma anotação @RequestBody.
     **/


    private static final Logger logger = LoggerFactory.getLogger(InvokerPerRequest.class);

    private final ObjectIdPerRequest objectIdPerRequest;

    public InvokerPerRequest(ObjectIdPerRequest objectIdPerRequest) {
        this.objectIdPerRequest = objectIdPerRequest;
    }

    public ResponseEntity<?> invoke(HttpRequest requestParam) {
        var httpMethod = requestParam.getHttpMethod();
        var path = requestParam.getPath();
        var jsonString = requestParam.getJsonData();
        var formData = requestParam.getFormData();

        try {
            var methodOpt = objectIdPerRequest.getMethod(httpMethod, path);
            Class<?> clazz;
            Object args;
            if ((methodOpt.isPresent())) {
                var method = methodOpt.get();
                clazz = method.getDeclaringClass();
                var obj = clazz.getDeclaredConstructor().newInstance();

                if (httpMethod.equals(HttpMethod.POST) && requestParam.isJsonData()) {
                    args = getRequestBodyParam(method, jsonString);
                    return (ResponseEntity<?>) method.invoke(obj, args);
                } else if(httpMethod.equals(HttpMethod.POST) && requestParam.isFormData()) {
                    args = getRequestBodyFormDataParam(method, formData);
                    return (ResponseEntity<?>) method.invoke(obj, args);
                } else {
                    return (ResponseEntity<?>) method.invoke(obj);
                }
            } else {
                logger.warn("Não foi possível encontrar o método solicitado.");
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Object getRequestBodyParam(Method method, String jsonString) {
        Parameter parameter = findAnnotatedParameter(method);
        if (parameter != null) {
            Class<?> parameterType = parameter.getType();
            return new MarshallerImpl().deserialize(jsonString, parameterType);
        }
        return null;
    }

    private Object getRequestBodyFormDataParam(Method method, Map<String, Object> formData) {
        Parameter parameter = findAnnotatedParameter(method);
        if (parameter != null) {
            Class<?> parameterType = parameter.getType();
            return new MarshallerImpl().deserializeFormData(formData, parameterType);
        }
        return null;
    }

    private Parameter findAnnotatedParameter(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(RequestBody.class))
                .findFirst()
                .orElse(null);
    }
}

