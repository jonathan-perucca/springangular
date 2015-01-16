package springangular.web.exception;

public enum ErrorCode {
    NO_ENTITY_DELETION(10001, "No entity found for deletion");
    
    private int code;
    private String description;
    
    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
