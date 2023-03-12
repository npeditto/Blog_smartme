package com.projects.blog.exceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;

abstract class ExceptionHandler {

    public ResponseEntity<Object> handle(Object error){
        HashMap<String, Object> errorOutput = new HashMap<>();

        errorOutput.put("date", LocalDateTime.now());
        errorOutput.put("error", error);

        return new ResponseEntity<>(errorOutput, HttpStatus.BAD_REQUEST);
    }

}
