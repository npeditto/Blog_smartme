package com.projects.blog.controllers;

import com.projects.blog.controllers.request.AuthorPutRequest;
import com.projects.blog.controllers.response.AuthorPatchResponse;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;
import com.projects.blog.models.User;
import com.projects.blog.resources.PostDTO;
import com.projects.blog.resources.UserDTO;
import com.projects.blog.resources.hateoas.PostDTOAssembler;
import com.projects.blog.resources.hateoas.AuthorPostDTOAssembler;
import com.projects.blog.services.interfaces.IPostService;
import com.projects.blog.services.interfaces.IUserService;
import com.projects.blog.utils.mappers.PostMapper;
import com.projects.blog.utils.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{id}/author")
public class AuthorPostController
{
    private final IPostService postService;
    private final IUserService userService;

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final AuthorPostDTOAssembler authorPostDTOAssembler;
    private final PostDTOAssembler postDTOAssembler;

    @GetMapping
    public ResponseEntity<EntityModel<UserDTO>> getAuthor(@PathVariable long id) throws ResourceNotFound {
        Post post = postService.getPost(id);
        UserDTO author = userMapper.toUserDTO(post.getAutore());

        return ResponseEntity.ok(authorPostDTOAssembler.toModel(author));
    }

    @PatchMapping
    public AuthorPatchResponse modifyAuthor(@PathVariable long id, @RequestBody @Valid AuthorPutRequest authorRequest) throws ResourceNotFound {
        Post post = postService.getPost(id);
        User author = userService.getUser(authorRequest.getAutore());
        post.setAutore(author);

        postService.update(id, post);

        AuthorPatchResponse authorPatchResponse = new AuthorPatchResponse();
        authorPatchResponse.setAuthor(userMapper.toUserDTO(author));

        return authorPatchResponse;
    }
}
