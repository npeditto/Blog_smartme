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

/**
 * Viene creato un oggetto che estende user, in questo caso
 * l'overloading serve per definire le rel. sull'autore
 */
@Component
public class AuthorPostDTOAssembler extends UserDTOAssembler {

    @Override
    public void addLinks(@NonNull EntityModel<UserDTO> user) {
        /**
         * Siccome ci si trova di fronte ad un autore bisogna anche aggiungere
         * la possibilità di modificare quell'autore (PATCH), per questo motivo
         * la classe UserDTOAssembler che contiene l'HATEOAS con self viene estesa
         * e viene aggiunta la possibilità di modificare l'autore.
         */
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
