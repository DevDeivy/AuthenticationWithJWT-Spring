package com.example.JWTauth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTservice {

    //nuestra clave secreta para firmar los tokens generados (para poder decodificarlos despues)
    public static final String SECRET_KEY ="123456789987654321Llasdadswqew12312321312asdsada213213asdas";

        // Este método genera un token solo con el usuario (sin claims extra)
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

        // Este método genera un token incluyendo claims adicionales
    private String getToken(Map<String,Object> extraClaim, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaim)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

        // Devuelve la clave a partir de la cadena codificada (usando Base64)
    private Key getKey() {
        byte[] keyByte= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

        // Extrae el nombre de usuario (subject) desde el token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

        // Valida que el token sea correcto: usuario coincida y no esté expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

        // Obtiene todos los claims (datos) del token
    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

        // Método genérico para obtener cualquier claim del token
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

        // Devuelve la fecha de expiración del token
    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    //verifica si el token ha expirado
    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
