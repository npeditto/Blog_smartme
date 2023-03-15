package com.projects.blog.auth.services;

import com.projects.blog.controllers.request.authentication.AuthenticationRequest;
import com.projects.blog.controllers.response.AuthenticationResponse;
import com.projects.blog.exceptionHandlers.exception.registration.UnderAgeException;
import com.projects.blog.exceptionHandlers.exception.resource.UserNotFound;
import com.projects.blog.models.User;
import com.projects.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User user) throws UnderAgeException, DataIntegrityViolationException
    {
        // Se la data di nascita dell'utente risulta essere più avanti di 18 anni fa questo implica che l'utente stesso è minorenne, ergo eccezione.
        if(user.data_nascita().isAfter(LocalDate.now().minusYears(18))){
            throw new UnderAgeException();
        }

        // Salvataggio dell'utente all'interno del database (persistenza).
        repository.save(user);

        return this.generateToken(user);
    }

    private AuthenticationResponse generateToken(User user){
        // Generazione del token a cui passerò l'utente da cui si prenderà il subj del token.
        String jwtToken = jwtService.generateToken(user);

        // Prelevo la data di scadenza del token dai claims del JWT
        Date expireDate = jwtService.extractExpireDate(jwtToken);

        // Rappresento sottoforma di stringa al fine di restituirla e avvisare l'utente della scadenza.
        String expDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expireDate);

        // Costruisco la risposta da restituire all'utente
        return AuthenticationResponse.builder()
                                     .token(jwtToken)
                                     .expire_date(expDateString)
                                     .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) throws UserNotFound {
        // Cerco l'utente per email (passata tramite richiesta validata), se non lo trovo lancio un errore
        // (gestito direttamente dagli Handler dichiarati nella cartella apposita).

        User user = repository.findByEmail(request.getEmail()).orElseThrow(UserNotFound::new);

        // Autentifico l'utente passando per il provider DAO (Data Access Object), tramite username
        // (in questo caso l'username è il PublicID, ovvero un campo univoco associato all'email passata dall'utente)
        // e password. Questo mi permetterà di autenticare l'utente qualora le credenziali fossero corrette.

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getPublicID(),
                        request.getPassword()
                )
        );

        return this.generateToken(user);
    }

}
