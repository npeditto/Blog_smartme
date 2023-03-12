package com.projects.blog.mocker;

import com.github.javafaker.Faker;
import com.projects.blog.models.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
public class PostGenerator implements Runnable  {

    private final List<Post> posts;
    private final int quantity;

    @Override
    public void run() {
        Faker faker = new Faker();

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        for (int i = 0; i < quantity; i++) {
            poolExecutor.execute(() -> {
                Post p = Post.builder().contenuto(faker.lorem().paragraph()).build();
                posts.add(p);
            });
        }

        poolExecutor.shutdown();
        try {
            poolExecutor.awaitTermination(10, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Finito Post");
    }
}
