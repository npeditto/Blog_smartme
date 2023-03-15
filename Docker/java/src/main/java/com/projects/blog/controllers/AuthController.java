package com.projects.blog.controllers;

import com.projects.blog.controllers.request.authentication.AuthenticationRequest;
import com.projects.blog.controllers.request.authentication.RegistrationRequest;
import com.projects.blog.controllers.response.AuthenticationResponse;
import com.projects.blog.exceptionHandlers.exception.registration.UnderAgeException;
import com.projects.blog.exceptionHandlers.exception.registration.UniqueEmailException;
import com.projects.blog.exceptionHandlers.exception.resource.UserNotFound;
import com.projects.blog.models.Role;
import com.projects.blog.models.User;
import com.projects.blog.auth.services.AuthenticationService;
import com.projects.blog.services.RoleService;
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

import java.util.UUID;

/**
 * Controller utilizzato per interagire con l'utente mediante chiamate HTTP.
 * Sarà lui ad utilizzare i servizi esposti dalla nostra applicazione Java al
 * fine di eseguire determinati compiti. Anche in questo caso viene utilizzato
 * lombok (@RequiredArgsConstructor) il quale permetterà di effettuare l'autowiring
 * con i servizi utilizzati. Inoltre, viene immesso un "prefix" /auth per
 * interagire con la risorsa -> /auth/register, /auth/login
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // Oggetto (Autowired) necessario per la codifica in BCrypt della password
    private final PasswordEncoder passwordEncoder;

    // Servizio per autenticare e/o registrare l'utente
    private final AuthenticationService authenticationService;

    // Servizio per interagire con i ruoli, questo ci permetterà di assegnare un ruolo all'utente.
    private final RoleService roleService;

    // Metodo richiamato quando l'utente effettua una richiesta POST alla route "/register"
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegistrationRequest userRequest) throws UniqueEmailException, UnderAgeException
    {
        try{
            // Generazione UUID (Universally Unique Identifier)
            final String uuid = UUID.randomUUID().toString().replace("-", "");

            // Dai metodi del servizio role, cerco il ruolo "utente" (default) e, se non lo trovo, lo creo.
            Role user_role = roleService.findOrCreateRole("utente");

            // Costruisco l'oggetto persistente User che dato in input all
            User user = User.builder()
                            .email(userRequest.getEmail())
                            .password(passwordEncoder.encode(userRequest.getPassword()))
                            .nome(userRequest.getNome())
                            .cognome(userRequest.getCognome())
                            .data_nascita(userRequest.getData_nascita())
                            .publicID(uuid) // Il Public ID verrà impiegato come subject del token.
                            .role(user_role)
                            .build();

            // Creazione della risposta con token
            AuthenticationResponse tokenRec = authenticationService.register(user);

            // Inoltro della risposta a cui passo la mia "AuthenticationResponse" e lo stato da restituire.
            return new ResponseEntity<>(tokenRec, HttpStatus.OK);
        }catch (DataIntegrityViolationException e){
            if(e.getMessage().contains("UniqueEmail")){
                throw new UniqueEmailException();
            }
        }
        return null;
    }

    // Metodo richiamato quando l'utente effettua una richiesta POST alla route "/login"
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) throws UserNotFound {
        // Metodo "login" ritorna il nuovo token solo se le credenziali sono esatte,
        // altrimenti solleva un'eccezione gestita dagli appositi handler nella cartella dedicata.
        return ResponseEntity.ok(authenticationService.login(request));
    }

}
