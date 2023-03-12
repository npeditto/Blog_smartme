package com.projects.blog.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder

@Entity @Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne
    @JoinColumn(name = "autore",nullable = false)
    @JsonBackReference
    private User autore;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Il contenuto del post non può risultare vuoto")
    private String contenuto;
}

