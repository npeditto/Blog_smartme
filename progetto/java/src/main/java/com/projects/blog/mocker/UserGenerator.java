package com.projects.blog.mocker;

import com.github.javafaker.Faker;
import com.projects.blog.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
        final String password = new BCryptPasswordEncoder().encode("password");

        // Generazione dei dati fake
        Faker faker = new Faker();
        String[] domains = {"hotmail.com", "outlook.it", "live.it", "yahoo.com", "gmail.com"};

        Set<String> uniqueEmails = new HashSet<>();

        // Per generare una quantità esatta di email e garantire univocità verrà effettuato un ciclo fino a quando il
        // set (insieme senza ripetizioni) sia uguale in dimensione al numero di utenti richiesti
        do{
            final String firstName = faker.name().firstName();
            final String lastName = faker.name().lastName();
            final String email = String.format("%s.%s@%s", firstName.toLowerCase(), lastName.toLowerCase(), extractRandom(domains));
            uniqueEmails.add(email);
        }while (uniqueEmails.size() < quantity);


        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        // Converto il set in lista
        List<String> emailList = uniqueEmails.stream().toList();

        // Generazione dei task da assegnare alla coda
        for (int i = 0; i < quantity; i++) {
            int finalI = i;

            // Task
            poolExecutor.execute(() -> {
                final String firstName = faker.name().firstName();
                final String lastName = faker.name().lastName();
                final String email = emailList.get(finalI);
                final LocalDate dataNascita = faker.date().birthday(18, 60).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // Public ID scelto per non mostrare nel subj del token l'id reale (ai fini di sicurezza, proteggo il meccanismo
                // che sta dietro la generazione degli ID).
                String uuid = UUID.randomUUID().toString().replace("-", "");

                // Costruisco l'utente

                User user = User.builder()
                                .nome(firstName)
                                .cognome(lastName)
                                .email(email)
                                .password(password)
                                .data_nascita(dataNascita)
                                .publicID(uuid)
                                .build();

                // Aggiungo l'utente alla lista che poi verrà caricata nel database
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
