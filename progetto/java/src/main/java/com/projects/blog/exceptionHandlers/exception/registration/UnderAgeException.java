package com.projects.blog.exceptionHandlers.exception.registration;

import com.projects.blog.exceptionHandlers.exception.BaseException;

/**
 * Eccezione impiegata per validare le richieste di registrazione al blog
 */
public class UnderAgeException extends BaseException {

    public UnderAgeException(){
        this("Risulti essere ancora minorenne per iscriverti al blog.");
    }

    public UnderAgeException(String message){
        super(message);
    }

}
