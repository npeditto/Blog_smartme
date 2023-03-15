package com.projects.blog.mocker;

import com.projects.blog.models.Post;
import com.projects.blog.models.Role;
import com.projects.blog.models.User;
import com.projects.blog.repositories.PostRepository;
import com.projects.blog.repositories.UserRepository;
import com.projects.blog.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * La CommandLineRunner è un'interfaccia che indica che un Bean deve essere avviato subito dopo che Spring
 * è completamente partito. In particolare questi vengono avviati solo se Spring Boot viene avviato da riga di comando.
 */

@Component
@RequiredArgsConstructor
public class Generator implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final RoleService roleService;

    /**
     * Metodo utilizzato per estrarre randomicamente un elemento da una lista di un tipo generico (metodo generico)
     * @param lista Lista di elementi
     * @return Elemento Randomico
     * @param <T> - Tipologia dell'elemento contenuto in lista
     */
    private <T> T extractRandom(List<T> lista){
        Random r = new Random();
        return lista.get(r.nextInt(lista.size()));
    }

    // Metodo run invocato una volta che l'applicazione Spring è avviata.
    @Override
    public void run(String... args) {
        // Lista sincronizzata per prevenire eventuali corse alle risorse causando quindi un'ambiguità sui dati.

        List<Post> posts = Collections.synchronizedList(new ArrayList<>());
        List<User> users = Collections.synchronizedList(new ArrayList<>());

        // Quantità delle risorse da generare
        int USERS_QTA = 200;
        int POSTS_QTA = (int) (USERS_QTA * 0.5);

        List<Runnable> tasks = List.of(
                new UserGenerator(users, USERS_QTA),
                new PostGenerator(posts, POSTS_QTA)
        );

        // Creazione di una pool di thread che attiverà i vari generatori.
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        for (Runnable task : tasks) {
            poolExecutor.execute(task);
        }

        // Chiudi la coda e aspetta che terminino i Thread
        poolExecutor.shutdown();
        try {
            poolExecutor.awaitTermination(10, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        // Cerca il ruolo di "utente" ed impostalo per ogni utente creato
        Role role = roleService.findOrCreateRole("utente");

        for (User user : users){
            user.setRole(role);
        }

        // Estrai un autore tra la lista degli autori ed impostano nei post.
        for (Post post : posts) {
            User author = extractRandom(users);
            post.setAutore(author);
        }

        // Salva tutto
        userRepository.saveAll(users);
        postRepository.saveAll(posts);

    }
}
