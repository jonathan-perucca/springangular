package com.jperucca.springangular.web.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class DataIntegrityException extends RuntimeException {
    
    public DataIntegrityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
