package com.projects.blog.services;


import com.projects.blog.exceptionHandlers.exception.resource.PostNotFound;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;
import com.projects.blog.repositories.PostRepository;
import com.projects.blog.services.interfaces.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository repository;

    @Override
    public Post getPost(long id) throws PostNotFound {
        return repository.findById(id).orElseThrow(PostNotFound::new);
    }

    @Override
    public List<Post> getPosts(){
        return repository.findAll();
    }

    @Override
    public void save(Post post){
        repository.save(post);
    }

    @Override
    public void delete(long id_post) throws ResourceNotFound {
        Post post = this.getPost(id_post);
        repository.delete(post);
    }

    @Override
    public void update(long id_post, Post updatedPost) throws ResourceNotFound {
        Post post = this.getPost(id_post);
        post.setContenuto(updatedPost.getContenuto());
        repository.saveAndFlush(post);
    }
}
