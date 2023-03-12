package com.projects.blog.mocker;

import com.github.javafaker.Faker;
import com.projects.blog.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class UserGenerator implements Runnable {
    private final List<User> users;
    private final int quantity;

    private String extractRandom(String[] array){
        Random r = new Random();
        return array[r.nextInt(array.length)];
    }


    @Override
    public void run() {
        // Primo Utente -- Usato per autenticare

        final String password = new BCryptPasswordEncoder().encode("password");

        User principal = User.builder()
                .nome("Emanuele")
                .cognome("Pannuccio")
                .email("pannuccio93@gmail.com")
                .password(password)
                .data_nascita(LocalDate.now().minusYears(22))
                .build();

        users.add(principal);

        Faker faker = new Faker();
        String[] domains = {"hotmail.com", "outlook.it", "live.it", "yahoo.com", "gmail.com"};

        Set<String> uniqueEmails = new HashSet<>();


        do{
            final String firstName = faker.name().firstName();
            final String lastName = faker.name().lastName();
            final String email = String.format("%s@%s.%s", firstName.toLowerCase(), lastName.toLowerCase(), extractRandom(domains));
            uniqueEmails.add(email);
        }while (uniqueEmails.size() < quantity);


        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        List<String> emailList = uniqueEmails.stream().toList();

        for (int i = 0; i < quantity; i++) {
            int finalI = i;
            poolExecutor.execute(() -> {
                final String firstName = faker.name().firstName();
                final String lastName = faker.name().lastName();
                final String email = emailList.get(finalI);
                final LocalDate dataNascita = faker.date().birthday(18, 60).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                User user = User.builder()
                        .nome(firstName)
                        .cognome(lastName)
                        .email(email)
                        .password(password)
                        .data_nascita(dataNascita)
                        .build();

                users.add(user);
            });
        }

        poolExecutor.shutdown();

        try {
            poolExecutor.awaitTermination(10, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }


        System.out.println("Finito Utente");
    }
}
