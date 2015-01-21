package com.jperucca.springangular.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import com.jperucca.springangular.web.exception.DataIntegrityException;
import com.jperucca.springangular.web.exception.ErrorInfo;
import com.jperucca.springangular.web.exception.NotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static com.jperucca.springangular.web.helper.HttpHelper.getUri;

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody 
    public ErrorInfo handleNotFound(WebRequest request, NotFoundException exception) {
        return new ErrorInfo.Builder(getUri(request), exception.getErrorCode()).build();
    }
    
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityException.class)
    @ResponseBody
    public ErrorInfo handleDateIntegrityException(WebRequest request, DataIntegrityException exception) {
        return new ErrorInfo.Builder(getUri(request), exception.getErrorCode()).build();
    }
}
