package ufrn.middleware.server;

import ufrn.middleware.utils.enums.HttpMethod;

/**
 * Represents a request parameter containing HTTP method, path, and JSON string data.
 *
 * <p>The `RequestParam` class is a record that encapsulates information about a request parameter,
 * including the HTTP method, path, and JSON string data. It is used to pass this information
 * between components of a remote communication system.
 *
 * @see HttpMethod
 */
public record RequestParam(HttpMethod httpMethod, String path, String jsonString) {
}
