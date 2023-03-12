package com.projects.blog.exceptionHandlers;

import com.projects.blog.exceptionHandlers.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class BaseExceptionHandler extends ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handle(BaseException e){
        return super.handle(e.getMessage());
    }

}
