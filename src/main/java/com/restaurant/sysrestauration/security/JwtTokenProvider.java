package com.restaurant.sysrestauration.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.impl.DefaultJwtParser;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private long validityInMilliseconds = 3600000;

    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username); // L'email ou le nom d'utilisateur
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    // Cette méthode retourne l'email à partir du JWT
    public String getEmailFromJWT(String token) {
        JwtParser jwtParser = Jwts.parser();
        Claims claims = jwtParser.setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Le 'subject' contient l'email
    }

    public boolean isTokenExpired(String token) {
        JwtParser jwtParser = Jwts.parser();
        Date expiration = jwtParser.setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
