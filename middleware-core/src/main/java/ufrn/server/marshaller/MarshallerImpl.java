package ufrn.server.marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ufrn.annotations.http.RequestParam;
import ufrn.exceptions.RemoteError;
import ufrn.utils.FileTypeAdapter;
import ufrn.utils.StringConverterUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MarshallerImpl implements Marshaller {

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

    @Override
    public Object deserializeFormData(Map<String, Object> body, Class<?> clazz) throws RemoteError {
        try {
            // Use o Gson para desserializar o JSON no tipo de objeto desejado.
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(File.class, new FileTypeAdapter())
                    .create();
            JsonElement jsonElement = gson.toJsonTree(body);
            return gson.fromJson(jsonElement, clazz);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteError("Erro na desserialização do JSON.", 500);
        }
    }

    @Override
    public Object[] deserializeRequestParams(Method method, Map<String, String> paramsMap) throws RemoteError {
        List<Object> argsList = new ArrayList<>();
        for (var param : method.getParameters()) {
            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            if (Objects.nonNull(requestParam)) {
                var fieldName = requestParam.name();
                if (!paramsMap.containsKey(fieldName))
                    throw new RemoteError("Parametro " + fieldName + " não encontrado!", 400);

                var paramValue = paramsMap.get(fieldName);
                var clazz = param.getType();

                argsList.add(StringConverterUtil.convert(paramValue, clazz));

            }
        }
        return argsList.toArray();
    }


}