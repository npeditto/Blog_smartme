package com.projects.blog.controllers.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
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
    @Past
    private LocalDate data_nascita;
}
