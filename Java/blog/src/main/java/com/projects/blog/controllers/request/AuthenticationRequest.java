package com.projects.blog.controllers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    @Email
    @NotEmpty(message = "Il campo email deve essere presente")
    private String email;

    @NotEmpty(message = "Il campo password deve essere presente.")
    @Size(min = 8, max = 255, message = "La dimensione della password deve essere compresa tra 8 e 255")
    private String password;
}
