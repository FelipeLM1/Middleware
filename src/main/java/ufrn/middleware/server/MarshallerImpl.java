package ufrn.middleware.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ufrn.middleware.exceptions.RemoteError;

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
}