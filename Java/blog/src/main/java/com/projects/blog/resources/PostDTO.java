package com.projects.blog.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * DTO - Data Transfer Object, ovvero un oggetto utilizzato per rappresentare
 * i dati che vengono trasferit da un livello applicativo ad un livello di persistenza
 * e viceversa. Questo ci permette quindi di avere una risorsa malleabile dando
 * il vantaggio di separare la rappresentazione della risorsa dalla risorsa stessa.
 */
@Data
public class PostDTO {
    /**
     * Notazione Jackson che permette di ignorare il campo durante la serializzazione, ovvero quel processo
     * che serve ma a convertire un oggetto in JSON.
     */
    @JsonIgnore
    private UserDTO autore;

    @JsonIgnore
    private long post_id;
    private String contenuto;
}
