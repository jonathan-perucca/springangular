package com.jperucca.springangular.web.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class NotFoundException extends RuntimeException{

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
