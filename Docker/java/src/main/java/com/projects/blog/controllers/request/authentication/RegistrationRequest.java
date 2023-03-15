package com.projects.blog.controllers.request.authentication;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegistrationRequest {
    /**
     * Creazione di una classe utilizzata per la validazione della richiesta di autenticazione.
     * In questo caso vengono inizializzati tutti i getter e setter (grazie all'annotazione @data),
     * Si ha quindi un costruttore con tutti gli attributi, un costruttore di default e grazie sempre a Lombok
     * una classe interna Builder che permette la costruzione di un metodo secondo il pattern "Builder" (costruisco
     * pian piano l'oggetto specificandone gli attributi)
     */

    @Email
    @NotEmpty(message = "Il campo email deve essere presente")
    private String email;


    @NotEmpty(message = "Il campo password deve essere presente.")
    @Size(min = 8, max = 255, message = "La dimensione della password deve essere compresa tra 8 e 255")
    private String password;

    @NotEmpty(message = "Il campo nome deve essere presente.")
    private String nome;

    @NotEmpty(message = "Il campo cognome deve essere presente.")
    private String cognome;

    @NotNull(message = "La data di nascita deve essere presente.")
    @Past // Notazione utilizzata per fare in modo che la data sia situata nel passato
    private LocalDate data_nascita;
}
