package com.projects.blog.controllers;

import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;
import com.projects.blog.models.User;
import com.projects.blog.resources.PostDTO;
import com.projects.blog.resources.hateoas.PostDTOAssembler;
import com.projects.blog.services.interfaces.IPostService;
import com.projects.blog.utils.mappers.PostMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final IPostService postService;

    private final PostMapper mapperService;

    private final PostDTOAssembler postDTOAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PostDTO>>> getPosts(){
        List<Post> posts = postService.getPosts();
        List<PostDTO> postDTOS = mapperService.toPostDTOList(posts);

        return ResponseEntity.ok(postDTOAssembler.toCollectionModel(postDTOS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PostDTO>> getPostByID(@PathVariable long id) throws ResourceNotFound {
        Post p = postService.getPost(id);

        PostDTO post = mapperService.toPostDTO(p);
        return ResponseEntity.ok(postDTOAssembler.toModel(post));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PostDTO>> createPost(@RequestBody @Valid Post post){
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAutore(user);
        postService.save(post);

        PostDTO postDTO = mapperService.toPostDTO(post);
        return ResponseEntity.ok(postDTOAssembler.toModel(postDTO));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable long id) throws ResourceNotFound {
        HashMap<String, String> res = new HashMap<>();
        res.put("ok", "Risorsa eliminata");

        postService.delete(id);
        return res;
    }

    @PutMapping("/{id}")
    public Map<String, String> update(@PathVariable long id, @RequestBody @Valid Post post) throws ResourceNotFound {
        HashMap<String, String> res = new HashMap<>();
        res.put("ok", "Risorsa modificata");

        postService.update(id, post);
        return res;
    }
}
