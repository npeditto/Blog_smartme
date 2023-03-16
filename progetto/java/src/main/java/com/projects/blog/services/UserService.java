package com.projects.blog.services;


import com.projects.blog.exceptionHandlers.exception.resource.UserNotFound;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.User;
import com.projects.blog.repositories.UserRepository;
import com.projects.blog.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;

    @Override
    public List<User> getUsers(){
        return repository.findAll();
    }

    @Override
    public User getUser(long id) throws ResourceNotFound {
        return repository.findById(id).orElseThrow(UserNotFound::new);
    }

}
