package com.projects.blog.services;


import com.projects.blog.exceptionHandlers.exception.resource.PostNotFound;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;
import com.projects.blog.repositories.PostRepository;
import com.projects.blog.services.interfaces.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Esposizione delle funzionalità legate alla repository di Post. Sarà questa a permettere
 * l'interazione con gli oggetti Post. Implemento le interfacce cosi posso utilizzare il meccanismo
 * di Spring per un cambio rapido delle classi (Dipendency Injection + Qualifier).
 */
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
    public void update(long id_post, String content) throws ResourceNotFound {
        Post post = this.getPost(id_post);
        post.setContenuto(content);
        repository.saveAndFlush(post);
    }
}
