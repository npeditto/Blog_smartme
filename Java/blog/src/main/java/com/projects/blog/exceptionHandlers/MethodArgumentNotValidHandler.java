package com.projects.blog.exceptionHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class MethodArgumentNotValidHandler extends com.projects.blog.exceptionHandlers.ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(MethodArgumentNotValidException ex){
        List<String> allErrors = new ArrayList<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> allErrors.add(error.getDefaultMessage()));

        return super.handle(allErrors);
    }

}
