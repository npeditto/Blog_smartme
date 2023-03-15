package com.projects.blog.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.blog.models.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO - Data Transfer Object, ovvero un oggetto utilizzato per rappresentare
 * i dati che vengono trasferit da un livello applicativo ad un livello di persistenza
 * e viceversa. Questo ci permette quindi di avere una risorsa malleabile dando
 * il vantaggio di separare la rappresentazione della risorsa dalla risorsa stessa.
 */

@NoArgsConstructor
@Setter
@Getter
public class UserDTO extends RepresentationModel<UserDTO> {
    /**
     * Notazione Jackson che permette di ignorare il campo durante la serializzazione, ovvero quel processo
     * che serve ma a convertire un oggetto in JSON.
     */

    @JsonIgnore
    private long user_id;

    private String email;

    private String nome;

    private String cognome;

    private LocalDate data_nascita;

    @JsonIgnore
    private List<Post> posts;

    @JsonIgnore
    public long getUserID() {
        return user_id;
    }
}
