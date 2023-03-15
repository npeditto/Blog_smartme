package com.projects.blog.config;

import com.projects.blog.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Data
@RequiredArgsConstructor
public class AppSecurityBeanConfig {

    private final UserRepository repository;

    /**
     * Metodo impiegato per creare un oggetto (generato con Lambda Expression)
     * il cui compito è prendere i dati dal database legati all'utente.
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repository.findByPublicID(username).orElseThrow(
                ()->new UsernameNotFoundException("Utente non trovato.")
        );
    }

    /**\
     * <a href="https://docs.spring.io/spring-security/reference/_images/servlet/authentication/unpwd/daoauthenticationprovider.png">...</a>
     * Spring Bean - Pojo method responsabile della restituzione di un AuthenticationProvider il cui compito è gestire
     * il login tramite username e password. Esso utilizza l'oggetto "userDetailsService" al fine di prendere le informazioni
     * necessarie per autenticare le informazioni dell'utente.
     *
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Metodo impiegato per la creazione di uno Spring Bean "AuthenticationManager" che mi permettererà di gestire
     * tutte le richieste di autenticazione. Sarà quest'ultimo a consultare tutti i provider disponibili
     * (DAOAuthenticationProvider) per effettuare l'autenticazione tramite l'uso del ProviderManager (che implementa
     * l'interfaccia dell'authentication manager).
     *
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean che mi permetterà di codificare la password ed effettuare il matching tra quella codificata del DB e
     * quella inserita dall'utente (Oggetto usato dal Provider).
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
