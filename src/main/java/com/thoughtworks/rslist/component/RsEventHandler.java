package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class RsEventHandler {

    public static Logger logger = LoggerFactory.getLogger(RsEventHandler.class);

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException) {
            errorMessage = "invalid param";
        } else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        logger.error(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

}
