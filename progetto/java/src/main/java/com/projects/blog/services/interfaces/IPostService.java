package com.projects.blog.services.interfaces;

import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;

import java.util.List;

public interface IPostService {

    List<Post> getPosts();

    Post getPost(long id) throws ResourceNotFound;

    void save(Post post);

    void delete(long id_post) throws ResourceNotFound;

    void update(long id_post, String content) throws ResourceNotFound;
}
