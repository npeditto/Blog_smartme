package com.projects.blog.services;

import com.projects.blog.controllers.request.AuthenticationRequest;
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
        if(user.data_nascita().isAfter(LocalDate.now().minusYears(18))){
            throw new UnderAgeException();
        }

        repository.save(user);

        String jwtToken = jwtService.generateToken(user);

        Date expireDate = jwtService.extractExpireDate(jwtToken);
        String expDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expireDate);

        return AuthenticationResponse.builder()
                                     .token(jwtToken)
                                     .expire_date(expDateString)
                                     .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) throws UserNotFound {
        User user = repository.findByEmail(request.getEmail()).orElseThrow(UserNotFound::new);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);

        Date expireDate = jwtService.extractExpireDate(jwtToken);
        String expDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expireDate);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expire_date(expDateString)
                .build();
    }

}
