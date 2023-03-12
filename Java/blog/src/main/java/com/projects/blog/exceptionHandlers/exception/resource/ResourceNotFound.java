package com.projects.blog.exceptionHandlers.exception.resource;

import com.projects.blog.exceptionHandlers.exception.BaseException;

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
