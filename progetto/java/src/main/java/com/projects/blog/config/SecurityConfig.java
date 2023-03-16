package com.projects.blog.config;

import com.projects.blog.auth.filters.JWTAuthFilter;
import com.projects.blog.auth.handler.CustomLogoutHandler;
import com.projects.blog.auth.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.HashMap;
import java.util.Map;


/**
 * Classe impiegata per la creazione di uno Spring Bean impiegato da
 * Spring Security, modulo spring, per autenticare le richieste. In
 * questo caso quindi viene generata una securityFilterChain che secondo
 * autentifica qualsiasi richiesta escluse alcune riportate nel pattern
 * "requestMatchers". Per fare in modo da creare una RESTful API la policy
 * di generazione della sessione è impostato su stateless e questo ha permesso
 * di creare ad ogni richiesta una sessione effimera.
 * <p>
 * Inoltre alla SecurityFilterChain (catena di filtri - presente già di default in Spring security)
 * è stato aggiunto il JWTAuthFilter (Autowired, grazie @RequiredArgsConstruct - annotazione lombok
 * che mi permette di generare un construttore della classe per tutti quei campi impostati come final
 * - l'autowired avviene per costruttore. ) e l'authentication provider che è visibile nel file
 * "SecurityConfig". Il "JWTAuthFilter" del package com.projects.blog.auth.filters permette di
 * autenticare le richieste e validare il token mediante l'uso del servizio esposto. L'authentication
 * provider che permette di fornire un metodo di autenticazione (DaoAuthnetication - Data Access Object Auth.)
 * che fornisce un supporto per un autenticazione di tipo username/password.
 *
 * @author siakoo
 */
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final CustomLogoutHandler customLogoutHandler;

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(new OrRequestMatcher(
                    // Pattern dei PATH che non necessitano di JWT
                    new AntPathRequestMatcher("/post/**", "GET"),
                    new AntPathRequestMatcher("/auth/**")
            )).permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .logout().logoutUrl("/auth/logout").addLogoutHandler(customLogoutHandler) // Per i meccanismi di logout non sono stati implementati Controller ma degli Handler, tramite interfacce apposite.
            .logoutSuccessHandler(customLogoutSuccessHandler)
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Aggiunta filtro per autenticazione JWT.
        return http.build();
    }

}
