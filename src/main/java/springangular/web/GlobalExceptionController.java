package springangular.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import springangular.web.exception.ErrorInfo;
import springangular.web.exception.NotFoundException;

import static springangular.web.helper.HttpHelper.getUri;

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody 
    public ErrorInfo handleNotFound(WebRequest request, NotFoundException exception) {
        return new ErrorInfo.Builder(getUri(request), exception.getErrorCode()).build();
    }
}
