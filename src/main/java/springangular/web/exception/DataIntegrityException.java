package springangular.web.exception;

public class DataIntegrityException extends RuntimeException {
    
    private ErrorCode errorCode;
    
    public DataIntegrityException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
