package com.projects.blog.controllers.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Creazione di una classe utilizzata per la validazione della richiesta di autenticazione.
 * In questo caso vengono inizializzati tutti i getter e setter (grazie all'annotazione @data),
 * Si ha quindi un costruttore con tutti gli attributi, un costruttore di default e grazie sempre a Lombok
 * una classe interna Builder che permette la costruzione di un metodo secondo il pattern "Builder" (costruisco
 * pian piano l'oggetto specificandone gli attributi)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    // Validazione di un email con il validatore di Hibernate
    @Email
    @NotEmpty(message = "Il campo email deve essere presente")
    private String email;

    @NotEmpty(message = "Il campo password deve essere presente.")
    @Size(min = 8, max = 255, message = "La dimensione della password deve essere compresa tra 8 e 255")
    private String password;
}
