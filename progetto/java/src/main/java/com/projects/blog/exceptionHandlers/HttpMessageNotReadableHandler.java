package com.projects.blog.exceptionHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HttpMessageNotReadableHandler extends com.projects.blog.exceptionHandlers.ExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handle(){
        return super.handle("È stato impossibile leggere il corpo della richiesta, si prega di includerlo.");

    }

}
