package com.projects.blog.service;

import com.projects.blog.model.Post;
import com.projects.blog.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService{

    @Autowired
    private PostRepository postRepository;

    public Post saveProduct(Post p){
        return postRepository.save(p);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

}
