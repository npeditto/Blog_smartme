package com.projects.blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

// Annotazioni Lombok per inizializzare l'oggetto ed includere un costruttore di default ed uno con tutti gli argomenti
@AllArgsConstructor
@NoArgsConstructor
// Pattern Builder nella classe
@Builder
// Creazione di tutti i getter e setter
@Data

// Dichiaro che, tramite JPA, è un entità e fa riferimento alla tabella "users"
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})
        }
)
public class User implements UserDetails {
    // Chiave primaria, auto-increment
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(nullable = false, name = "email")
    private String email;

    // Deve essere unique ed inoltre non può assumere valori null
    @Column(nullable = false, unique = true)
    private String publicID;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private LocalDate data_nascita;

    // Relazione di una a molti con i post. Un utente ha fino a N post. In questo caso non è unidirezionale.
    @OneToMany(mappedBy = "autore", fetch = FetchType.EAGER)
    @JsonManagedReference
    @ToString.Exclude
    private List<Post> posts;

    // Relazione inversa di una a molti. In questo caso unidirezionale con il ruolo
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(this.role.getName())
        );
    }

    public LocalDate data_nascita() {
        return data_nascita;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }

    // Username impiegato per identificare univocamente il contesto, questo verrà conservato all'interno del contesto
    // di sicurezza
    @Override
    public String getUsername() {
        return this.publicID;
    }

    /**
     * Ai fini del progetto sono stati utilizzati dei valori di default per le funzioni sottostanti.
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
