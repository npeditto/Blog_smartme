package com.projects.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class User implements {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private LocalDate data_nascita;

    @OneToMany(mappedBy = "autore",cascade = CascadeType.ALL)
    private List<Post> posts;

}
