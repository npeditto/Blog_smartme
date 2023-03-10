package com.projects.blog.dummy_gen;

import com.github.javafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import

import java.util.Random;

@Component
public class Generator{
    private<T> T extractRandom(T[] elements){
        Random r = new Random();
        return elements[r.nextInt() % elements.length];
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        Faker f = new Faker();

        String firstName = f.name().firstName();
        String lastName = f.name().lastName();

        String[] domains = {"gmail.com", "hotmail.it", "outlook.com", "live.it", "libero.it", "yahoo.com"};
        String email = String.format("%s.%s@%s", firstName, lastName, extractRandom(domains));

        String password =
    }
}
