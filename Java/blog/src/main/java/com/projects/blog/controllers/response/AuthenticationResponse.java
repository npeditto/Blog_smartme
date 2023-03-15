package com.projects.blog.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    /**
     * Risposta restituita quando viene richiesto un token, viene anche inclusa la data di scadenza del token.
     */
    private String token;
    private String expire_date;

}
