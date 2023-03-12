package com.projects.blog.services.interfaces;

import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();

    User getUser(long id) throws ResourceNotFound;
}
