package ufrn.methods.perRequestLifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.annotations.http.RequestBody;
import ufrn.server.marshaller.MarshallerImpl;
import ufrn.server.RequestParam;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

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

    public ResponseEntity<?> invoke(RequestParam requestParam) {
        var httpMethod = requestParam.httpMethod();
        var path = requestParam.path();
        var jsonString = requestParam.jsonString();

        try {
            var methodOpt = objectIdPerRequest.getMethod(httpMethod, path);
            Class<?> clazz;
            Object args;
            if ((methodOpt.isPresent())) {
                var method = methodOpt.get();
                clazz = method.getDeclaringClass();
                var obj = clazz.getDeclaredConstructor().newInstance();

                if (httpMethod.equals(HttpMethod.POST) && Objects.nonNull(jsonString)) {
                    args = getRequestBodyParam(method, jsonString);
                    return (ResponseEntity<?>) method.invoke(obj, args);
                } else if (Objects.isNull(jsonString)) {
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
        Parameter parameter = findAnnotatedParameter(method, RequestBody.class);
        if (parameter != null) {
            Class<?> parameterType = parameter.getType();
            return new MarshallerImpl().deserialize(jsonString, parameterType);
        }
        return null;
    }

    private Parameter findAnnotatedParameter(Method method, Class<? extends Annotation> annotationType) {
        return Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(annotationType))
                .findFirst()
                .orElse(null);
    }
}

