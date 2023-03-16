package com.projects.blog.auth.filters;

import com.projects.blog.auth.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * Filtro di sicurezza impiegato per l'implementazione di un sistema di autenticazione basato su JWT.
 * Questa classe viene impiegata per ogni richiesta HTTP intercettata (OncePerRequestFilter).
 *
 */
@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Implementazione di un filtro di autenticazione (non sono solo prettamente di autenticazione, ma possono anche
     * effettuare operazioni di caching, comprimere i dati ndella risposta ed altro).
     *
     * @param request Oggetto che rappresenta la richiesta ricevuta, contiene tutti i dati passati in essa.
     * @param response Oggetto che permette la costruzione della risposta inviata dal server HTTP al client.
     * @param filterChain Oggetto di tipo "Filterchain" (Interfaccia Java) utilizzato come componente per interagire
     *                    con la catena di filtri esistenti. Questo sarà utile per passare al filtro successivo.
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Prendo l'header contenuto nella richiesta "Authorization" in cui mi aspetto di trovare il Bearer Token
        final String authHeader = request.getHeader("Authorization");
        // Dichiaro le stringhe "token" e "subject" che mi permettono di conservare rispettivamente il token ed il possessore del token
        final String token, subject;

        // Se non vi è l'header di autenticazione o non vi è un Bearer Token (solitamente un JWT viene passato sottoforma di Bearer Token).
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            // Passa al filtro successivo ma non autenticare l'utente che ha fatto richiesta. (Impossibilitando l'accesso alla risorsa)
            filterChain.doFilter(request, response);
            return;
        }

        // Prendo il token e lo pulisco da eventuali spazi non previsti
        token = authHeader.split(" ")[1].trim();

        // Utilizzo il JWTService (ovvero un servizio che mi espone le funzionalità per interagire con il token) per estrapolarmi il subject (richiedente dell'operazione)
        subject = jwtService.extractSubject(token);

        /**
         * Ottengo l'attuale contesto di sicurezza grazie al SecurityContextHolder. Viene adottata una strategia "Thread-Local", nella quale per ogni utente
         * l'oggetto SecurityContextHolder manterrà un contesto di Authenticazione sui thread corrispondenti dato che ogni richiesta avrà un thread
         * a se stante. Quindi, verrà solamente impiegato per gestire l'autenticazione e l'autorizzazione per quella richiesta
         */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        /**
         * Se il soggetto è stato trovato nel token e l'autenticazione non è stata effettuata, allora
         * mi prendo le informazioni dal database tramite l'oggetto "UserDetailService" configurato
         * nella classe "AppSecurityBeanConfig".
         */
        if (subject != null && auth == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject);

            /**
             * Verifico che il token sia valido grazie al metodo fornito dall'oggetto (Autowired con notazione lombok)
             * "JWTService".
             */
            if(jwtService.isValid(token, userDetails)){
                /**
                 * Se il token è valido allora imposto il contesto del thread corrente con "UsernamePasswordAuthenticationToken" che mi permetterà di
                 * autentificare l'utente. (In questo caso quindi risulterà già autenticato.
                 */
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                /**
                 * Serve per creare un oggetto che conterrà tutti i dettagli relativi alla richiesta HTTP utilizzata per autenticare l'utente. I detaggli includono
                 * informazioni come l'indirizzo IP e il browser che l'utente sta impiegando. Questi vengono conservati insieme alla richiesta autenticata
                 * (UsernamePasswordAuthenticationToken).
                 */
                WebAuthenticationDetails web = new WebAuthenticationDetailsSource().buildDetails(request);

                authToken.setDetails(web);

                // Aggiorno il principal e l'utente risulterà autenticato.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // In qualsiasi caso, se il token non è valido semplicemente non autentifico l'utente e vado avanti nella security chain.
        filterChain.doFilter(request, response);

    }
}
