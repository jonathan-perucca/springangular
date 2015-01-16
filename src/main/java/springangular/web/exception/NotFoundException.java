package springangular.web.exception;

public class NotFoundException extends RuntimeException{

    private ErrorCode errorCode;
    
    public NotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
