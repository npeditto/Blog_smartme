package com.projects.blog.resources.hateoas;

import com.projects.blog.controllers.UserController;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.resources.PostDTO;
import com.projects.blog.resources.UserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserPostDTOAssembler extends PostDTOAssembler {

    @Override
    public void addLinks(CollectionModel<EntityModel<PostDTO>> resources)
    {
        Optional<EntityModel<PostDTO>> post = resources.getContent().stream().findFirst();

        if(post.isPresent()){
            UserDTO autore = Objects.requireNonNull(post.get().getContent()).getAutore();
            long userID = autore.getUserID();

            try {
                resources.add(
                        linkTo(methodOn(UserController.class).getPostsByUser(userID)).withSelfRel()
                );
            } catch (ResourceNotFound e) {
                throw new RuntimeException(e);
            }
        }

    }
}
