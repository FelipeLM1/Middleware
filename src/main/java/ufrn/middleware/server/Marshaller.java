package ufrn.middleware.server;

import com.google.gson.JsonObject;
import ufrn.middleware.exceptions.RemoteError;

public interface Marshaller {
    public byte[] serialize(JsonObject message);

    public JsonObject deserialize(String body) throws RemoteError;
}


