package com.projects.blog.controllers.request.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPostRequest {
    // Il campo contenuto non può essere vuoto, quindi se anteceduto da un @Valido @RequestBody questo oggetto andrà a
    // verificare l'esistenza del campo.

    @NotEmpty(message = "Il contenuto del post non può risultare vuoto")
    private String contenuto;

}
