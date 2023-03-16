package com.projects.blog.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Cancellazione del contesto di sicurezza per evitare che l'utenta possa accedere, essendo autenticato, alle risorse.
        SecurityContextHolder.clearContext();

        // Creazione JSON Payload

        Map<String, String> json = new HashMap<>();

        if(response.getStatus() == HttpStatus.OK.value()){
            json.put("ok", "Token invalidato, logout effettuato con successo.");
        }else{
            json.put("error", "Token non valido o gia revocato.");
        }

        String jsonPayload = objectMapper.writeValueAsString(json);
        ResponseEntity<String> resp = new ResponseEntity<>(jsonPayload, HttpStatus.OK);

        response.setContentType("application/json");
        response.getWriter().write(Objects.requireNonNull(resp.getBody()));
        response.setStatus(resp.getStatusCode().value());


    }
}
