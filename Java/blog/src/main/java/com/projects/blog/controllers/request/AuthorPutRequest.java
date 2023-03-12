package com.projects.blog.controllers.request;

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
    @NotNull
    @DecimalMin(value = "1", message = "Il campo autore deve essere presente e valido.")
    private long autore;
}
