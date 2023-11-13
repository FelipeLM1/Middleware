package ufrn.exceptions;

public class InternalErrorException extends RemoteError {
    public InternalErrorException(String error, int code) {
        super(error, code);
    }
}
