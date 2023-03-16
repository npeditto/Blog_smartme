package com.projects.blog.exceptionHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Classe utilizzata per gestire l'errore "BadCredential" generato quando le credenziali
 * passate al login risultano sbagliate. In questo caso ControllerAdvice è una estensione
 * di Component che permette di gestire le eccezioni tramite le annotazioni @ExceptionHandler
 */
@ControllerAdvice
public class BadCredentialsHandler extends com.projects.blog.exceptionHandlers.ExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handle(){
        return super.handle("Le credenziali inserite risultano sbagliate.");
    }

}
