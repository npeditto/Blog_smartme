package com.projects.blog.services;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class JWTService {

    private final int expireMs = 60 * 60 * 1000; // 1h durata json web token

    // Chiave segreta utilizzata per il calcolo del JWT
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6150645367566B597033733676397924";

    // Utilizzo un metodo di decodifica per aggiungere un altro livello di complessità per l'individuazione della chiave.
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    /**
     * Metodo per generare il token che verrà utilizzato per autenticare le richieste
     * @param claims Oggetti contenuti all'interno del token JWT che riportano alcune informazioni
     * @param userDetails Dettagli riguardo l'utente che si è autenticato
     * @return JSON Web Token - Stringa alfanumerica firmata in combinazione alla chiave SECRET_KEY con un algoritmo HS256
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        Date actualDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + expireMs);

        // Costruzione mediante Builder per migliorare la lettura del codice.

        return Jwts.builder()
                   .addClaims(claims)
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt(actualDate)
                   .setExpiration(expireDate)
                   .signWith(getKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public String generateToken(UserDetails user){
        return this.generateToken(new HashMap<>(), user);
    }

    /**
     * TOKEN GET INFO
     */

    private Claims getClaims(String token){
        // Creo un parser che riesca a leggere il contenuto del JWT da cui estrapolo delle informazioni chiamate "Claims".
        // È necessario passare la chiave segreta. Solitamente qui viene riportato il Sub, iat (Issued at) e exp (data di
        // scadenza del JWT).
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        return parser.parseClaimsJws(token).getBody();
    }

    public String extractSubject(String token) {
        try{
            Claims claims = getClaims(token);
            return claims.getSubject();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Date extractExpireDate(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }


    public boolean isValid(String token, UserDetails user){
        final String subject = extractSubject(token);

        // Valido solo se corrisponde l'utente che si sta loggando al possessore del token, e non è scaduto.
        return subject != null && subject.equals(user.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return extractExpireDate(token).before(new Date());
    }

}
