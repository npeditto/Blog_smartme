package com.projects.blog.controllers;

import com.projects.blog.models.User;
import com.projects.blog.resources.UserDTO;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.resources.hateoas.UserDTOAssembler;
import com.projects.blog.services.interfaces.IUserService;
import com.projects.blog.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    private final UserMapper userMapper;

    private final UserDTOAssembler userDTOAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getUsers(){
        List<UserDTO> userDTOS = userMapper.toUserDTOList(userService.getUsers());
        return ResponseEntity.ok(userDTOAssembler.toCollectionModel(userDTOS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUser(@PathVariable long id) throws ResourceNotFound
    {
        User user = userService.getUser(id);
        EntityModel<UserDTO> userEntity = userDTOAssembler.toModel(userMapper.toUserDTO(user));
        return ResponseEntity.ok(userEntity);
    }
}
