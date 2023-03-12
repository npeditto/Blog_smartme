package com.projects.blog.resources.hateoas;

import com.projects.blog.controllers.AuthorPostController;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.resources.UserDTO;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorPostDTOAssembler extends UserDTOAssembler {

    @Override
    public void addLinks(@NonNull EntityModel<UserDTO> user) {
        super.addLinks(user);

        long userID = Objects.requireNonNull(user.getContent()).getUserID();

        try {
            Link authorPatch = linkTo(
                    methodOn(AuthorPostController.class).modifyAuthor(userID, null)
            ).withRel("patch");

            user.add(authorPatch);

        } catch (ResourceNotFound e) {
            throw new RuntimeException(e);
        }
    }

}
