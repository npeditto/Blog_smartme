package com.projects.blog.controllers.request.author;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorPutRequest {
    /**
     * Oggetto creato ad-hoc per la validazione delle richieste fatte con il metodo
     * PUT alla risorsa Author
     */
    @NotNull
    @DecimalMin(value = "1", message = "Il campo autore deve essere presente e valido.")
    private long autore;
}
