package com.projects.blog.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "autore")
    @JsonBackReference
    private User autore;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenuto;
}

