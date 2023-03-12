package com.projects.blog.mocker;

import com.projects.blog.models.Post;
import com.projects.blog.models.User;
import com.projects.blog.repositories.PostRepository;
import com.projects.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class Generator implements CommandLineRunner {


    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private <T> T extractRandom(List<T> lista){
        Random r = new Random();
        return lista.get(r.nextInt(lista.size()));
    }

    @Override
    public void run(String... args) {
        List<Post> posts = Collections.synchronizedList(new ArrayList<>());
        List<User> users = Collections.synchronizedList(new ArrayList<>());

        int USERS_QTA = 200;
        int POSTS_QTA = (int) (USERS_QTA * 0.5);

        List<Runnable> tasks = List.of(
                new UserGenerator(users, USERS_QTA),
                new PostGenerator(posts, POSTS_QTA)
        );

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        for (Runnable task : tasks) {
            poolExecutor.execute(task);
        }
        poolExecutor.shutdown();

        try {
            poolExecutor.awaitTermination(10, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        for (Post post : posts) {
            User author = extractRandom(users);
            post.setAutore(author);
        }

        userRepository.saveAll(users);
        postRepository.saveAll(posts);

    }
}
