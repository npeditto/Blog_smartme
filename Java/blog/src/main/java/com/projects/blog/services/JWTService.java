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

    private final int expireMs = 60 * 60 * 1000; // 1h duration json web token

    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6150645367566B597033733676397924";

//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        Date actualDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + expireMs);

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
        System.out.println(claims.getExpiration().getTime());
        return claims.getExpiration();
    }


    public boolean isValid(String token, UserDetails user){
        final String subject = extractSubject(token);
        return subject != null && subject.equals(user.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return extractExpireDate(token).before(new Date());
    }

}
