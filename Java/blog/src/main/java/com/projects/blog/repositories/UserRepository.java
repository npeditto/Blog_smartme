package com.projects.blog.repositories;

import com.projects.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Strategia basata sull'analisi del nome del methodo, JPA andrà a
     * generare la query per il campo con colonna "email"
     */
    Optional<User> findByEmail(String email);
}
