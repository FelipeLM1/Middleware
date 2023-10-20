package ufrn.middleware.server.broker;

import com.google.gson.JsonObject;
import ufrn.middleware.server.broker.exceptions.RemoteError;

public interface Marshaller {
    public byte[] serialize(JsonObject message);

    public JsonObject deserialize(String body) throws RemoteError;
}


