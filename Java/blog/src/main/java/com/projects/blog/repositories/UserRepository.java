package com.projects.blog.repositories;

import com.projects.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaccia per interagire con e JPA e che permette di eseguire i metodi CRUD sulle entità
 * del database. Vi sono metodi come "findOne", "findAll" ed è possibile comporne altri tramite
 * una composizione sintattica del nome del metodo come mostrato in altri come RoleRepository o
 * questa. I tipi generici passati sono rispettivamente il modello JPA e la tipologia della
 * chiave primaria impiegata.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Strategia basata sull'analisi del nome del methodo, si andrà a
     * generare la query per il campo con colonna "email"
     */
    Optional<User> findByEmail(String email);

    Optional<User> findByPublicID(String username);
}
