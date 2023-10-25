package ufrn.middleware.server;

import com.google.gson.JsonObject;
import ufrn.middleware.exceptions.RemoteError;

public interface Marshaller {
    public byte[] serialize(JsonObject message);

    public Object deserialize(String body, Class<?> clazz) throws RemoteError;
}


