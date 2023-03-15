package com.projects.blog.resources.hateoas;

import com.projects.blog.controllers.AuthorPostController;
import com.projects.blog.controllers.PostController;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.resources.PostDTO;
import lombok.NonNull;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostDTOAssembler implements SimpleRepresentationModelAssembler<PostDTO> {
    @Override
    public void addLinks(@NonNull EntityModel<PostDTO> resource) {
        PostDTO post = Objects.requireNonNull(resource).getContent();

        /**
         * Aggiunta dell'HATEOAS per la risorsa post, quindi aggiungo self,
         * create, update, delete, autore.
         */

        try {
            long postID = Objects.requireNonNull(post).getPost_id();

            Link selfRel = linkTo(methodOn(PostController.class).getPostByID(postID)).withSelfRel();

            Link createRel = linkTo(methodOn(PostController.class).createPost(null)).withRel("create");

            Link updRel = linkTo(methodOn(PostController.class).update(postID, null)).withRel("update");

            Link delRel = linkTo(methodOn(PostController.class).delete(postID)).withRel("delete");

            Link authorRel = linkTo(methodOn(AuthorPostController.class).getAuthor(postID)).withRel("autore");

            List<Link> arr = List.of(
                    selfRel,
                    createRel,
                    updRel,
                    delRel,
                    authorRel
            );

            arr.forEach(resource::add);

        } catch (ResourceNotFound e) {
            System.out.println("L'autore associato al post non è stato trovato.");
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<PostDTO>> resources) {
        Link selfRel = linkTo(methodOn(PostController.class).getPosts()).withSelfRel();

        resources.add(selfRel);
    }
}
