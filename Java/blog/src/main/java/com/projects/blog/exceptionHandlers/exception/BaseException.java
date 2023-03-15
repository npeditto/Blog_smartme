package com.projects.blog.exceptionHandlers.exception;

/**
 * Viene creato una BaseException in maniera tale da facilitarne la cattura
 * agli Handler ed essere il più specifico. Si sta creando quindi una famiglia
 * di eccezioni che hanno le stesse caratteristiche e che quindi (nonostante
 * risultino specifici per un determinato caso) riescono a condividere dei tratti
 * e a dare al contempo diverse informazioni sulla natura dell'eccezione.
 */
public class BaseException extends Exception {

    public BaseException(String message){
        super(message);
    }

}
