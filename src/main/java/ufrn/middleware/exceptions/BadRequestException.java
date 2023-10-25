package ufrn.middleware.exceptions;

public class BadRequestException extends RemoteError {
    public BadRequestException(String error, int code) {
        super(error, code);
    }
}
