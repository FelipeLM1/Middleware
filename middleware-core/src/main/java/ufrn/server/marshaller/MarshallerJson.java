package ufrn.server.marshaller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.annotations.http.ParamName;
import ufrn.exceptions.RemoteError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class MarshallerJson implements Marshaller {


    private static final Logger logger = LoggerFactory.getLogger(MarshallerJson.class);

    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(JsonObject message) {
        String jsonString = message.toString();
        return jsonString.getBytes();
    }

    @Override
    public Object deserialize(String body, Class<?> clazz) throws RemoteError {
        try {
            // Use o Gson para desserializar o JSON no tipo de objeto desejado.
            return gson.fromJson(body, clazz);
        } catch (Exception e) {
            throw new RemoteError("Erro na desserialização do JSON.", 500);
        }
    }

    public Object[] deserialize(String json, Parameter[] parameters) throws RemoteError {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Object[] result = new Object[parameters.length];

        try {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                String key = getParamName(parameter);

                Class<?> type = parameter.getType();

                if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
                    throw new RemoteError("Missing value for key: " + key, 400);
                }

                var deserializedValue = gson.fromJson(jsonObject.get(key), type);
                logger.debug(key);
                logger.debug(deserializedValue.toString());
                result[i] = deserializedValue;
            }
            return result;

        } catch (Exception e) {
            logger.error(e.toString());
            throw new RemoteError("Error deserializing JSON", 500);
        }
    }

    private String getParamName(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof ParamName) {
                return ((ParamName) annotation).value();
            }
        }
        return parameter.getName();
    }

}