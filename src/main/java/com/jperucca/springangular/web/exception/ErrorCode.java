package com.jperucca.springangular.web.exception;

public enum ErrorCode {
    NO_ENTITY_FOUND("No entity found"),
    WRONG_ENTITY_INFORMATION("Could not create/update entity with wrong informations");
    
    private String message;
    
    private ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
