package com.projects.blog.controller;

import com.projects.blog.model.Post;
import com.projects.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getPosts(){
        return postService.findAll();
    }

//
//    public Post addPost(Post p){
//        return postService.saveProduct(p);
//    }

}
