package ufrn.server.marshaller;

import com.google.gson.JsonObject;
import ufrn.exceptions.RemoteError;

/**
 * An interface for marshalling and unmarshalling JSON data.
 *
 * <p>The `Marshaller` interface defines methods for serializing a JSON object into a byte array
 * and deserializing a JSON string into an object of a specified class. It is intended for use
 * in remote communication scenarios where data needs to be transformed between JSON and Java objects.
 *
 * @see JsonObject
 */
public interface Marshaller {

    /**
     * Serialize a JSON object into a byte array.
     *
     * @param message The JSON object to be serialized.
     * @return A byte array containing the serialized JSON data.
     */
    public byte[] serialize(JsonObject message);

    /**
     * Deserialize a JSON string into an object of the specified class.
     *
     * @param body  The JSON string to be deserialized.
     * @param clazz The class type to which the JSON data should be deserialized.
     * @return An instance of the specified class containing the deserialized data.
     * @throws RemoteError If there's an error during deserialization.
     */
    Object deserialize(String body, Class<?> clazz) throws RemoteError;
}


