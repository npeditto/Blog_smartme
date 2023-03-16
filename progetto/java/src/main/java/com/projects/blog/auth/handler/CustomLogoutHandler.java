package com.projects.blog.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.blog.models.Token;
import com.projects.blog.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        // Dichiaro le stringhe "token" e "subject" che mi permettono di conservare rispettivamente il token ed il possessore del token
        final String token;

        // Se non vi è l'header di autenticazione o non vi è un Bearer Token (solitamente un JWT viene passato sottoforma di Bearer Token).
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            // Passa al filtro successivo ma non autenticare l'utente che ha fatto richiesta. (Impossibilitando l'accesso alla risorsa)
            return;
        }

        // Prendo il token e lo pulisco da eventuali spazi non previsti
        token = authHeader.split(" ")[1].trim();
        Token tokenJPA = tokenRepository.findByToken(token).orElse(null);

        if (tokenJPA != null){
            // Segno nel database che il token è stato revocato e quindi non è più utilizzabile
            tokenJPA.setRevoked_at(LocalDateTime.now());
            tokenRepository.save(tokenJPA);
        }else{
            // Se non trovo il token, passerò comunque la richiesta al "LogoutSuccessHandler" al quale segnalerò che la richiesta non ha avuto esito positivo.
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
        }
    }
}
