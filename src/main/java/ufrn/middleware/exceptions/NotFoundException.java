package ufrn.middleware.exceptions;

public class NotFoundException extends RemoteError{
    public NotFoundException(String error, int code) {
        super(error, code);
    }
}
