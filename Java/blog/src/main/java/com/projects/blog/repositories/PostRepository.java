package com.projects.blog.repositories;

import com.projects.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaccia per interagire con e JPA e che permette di eseguire i metodi CRUD sulle entità
 * del database. Vi sono metodi come "findOne", "findAll" ed è possibile comporne altri tramite
 * una composizione sintattica del nome del metodo come mostrato in altri come RoleRepository o
 * UserRepository. I tipi generici passati sono rispettivamente il modello JPA e la tipologia della
 * chiave primaria impiegata.
 */
public interface PostRepository extends JpaRepository<Post, Long> {}
