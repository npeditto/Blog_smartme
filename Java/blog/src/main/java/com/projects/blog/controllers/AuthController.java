package com.projects.blog.controllers;

import com.projects.blog.controllers.request.AuthenticationRequest;
import com.projects.blog.controllers.request.RegistrationRequest;
import com.projects.blog.controllers.response.AuthenticationResponse;
import com.projects.blog.exceptionHandlers.exception.registration.UnderAgeException;
import com.projects.blog.exceptionHandlers.exception.registration.UniqueEmailException;
import com.projects.blog.exceptionHandlers.exception.resource.UserNotFound;
import com.projects.blog.models.User;
import com.projects.blog.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegistrationRequest userRequest) throws UniqueEmailException, UnderAgeException
    {
        try{
            User user = User.builder()
                            .email(userRequest.getEmail())
                            .password(passwordEncoder.encode(userRequest.getPassword()))
                            .nome(userRequest.getNome())
                            .cognome(userRequest.getCognome())
                            .data_nascita(userRequest.getData_nascita())
                            .build();

            AuthenticationResponse tokenRec = service.register(user);
            return new ResponseEntity<>(tokenRec, HttpStatus.OK);
        }catch (DataIntegrityViolationException e){
            if(e.getMessage().contains("UniqueEmail")){
                throw new UniqueEmailException();
            }
        }
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) throws UserNotFound {
        return ResponseEntity.ok(service.login(request));
    }

}
