package com.projects.blog.resources.hateoas;

import com.projects.blog.controllers.UserController;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.resources.UserDTO;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDTOAssembler implements SimpleRepresentationModelAssembler<UserDTO> {
    @Override
    public void addLinks(@NonNull EntityModel<UserDTO> user) {
        long userID = Objects.requireNonNull(user.getContent()).getUserID();
        try {
            user.add(
                    linkTo(methodOn(UserController.class).getUser(userID)).withSelfRel()
            );

            user.add(
                    linkTo(methodOn(UserController.class).getPostsByUser(userID)).withRel("posts")
            );

        } catch (ResourceNotFound e) {
            System.out.println("Errore UserDTOAssembler, fallito il caricamento di utente con id " + userID + ".");
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDTO>> resources) {
        Link selfCollection = linkTo(methodOn(UserController.class).getUsers()).withSelfRel();
        resources.add(selfCollection);

    }
}
