package com.projects.blog.exceptionHandlers.exception.registration;

import com.projects.blog.exceptionHandlers.exception.BaseException;

public class UniqueEmailException extends BaseException {

    public UniqueEmailException(){
        this("L'utente inserito è già presente all'interno dei nostri database.");
    }

    public UniqueEmailException(String message){
        super(message);
    }


}
