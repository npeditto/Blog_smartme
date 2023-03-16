package com.projects.blog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long token_id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expire_date;

    private LocalDateTime revoked_at;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User owner;

    public Boolean isRevoked(){
        return revoked_at != null;
    }
}
