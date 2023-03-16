package com.projects.blog.exceptionHandlers.exception.resource;

public class TokenNotFound extends ResourceNotFound {

    public TokenNotFound(){
        super("token");
    }

}
