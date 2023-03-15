package com.projects.blog.exceptionHandlers.exception.resource;

import com.projects.blog.exceptionHandlers.exception.BaseException;

/**
 * Eccezione utilizzata per creare diverse eccezioni da lanciare con un messaggio di
 * default "La risorsa non è stata trovata..."
 */
public class ResourceNotFound extends BaseException {

    public ResourceNotFound(){
        this("La risorsa selezionata non risulta presente nei nostri database.");
    }

    public ResourceNotFound(String type){
        super(
                String.format("La risorsa %s richiesta non risulta essere presente nel sistema.", type)
        );
    }

}
