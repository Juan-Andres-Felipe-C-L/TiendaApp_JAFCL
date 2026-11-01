package com.JuanAndresFelipe.TiendaApp.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
    
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token-expiration}")
    private Long tokenExpiration;

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Se utilizan los siguientes atributos para generar el JWT token.
     * @param userId
     * @param username
     * @param rolid
     * @return un String JWT token.
     */
    public String generateToken(Long userId, String username, Long rolId) {
       return Jwts.builder()
                .claims(Map.of("userId", userId))
                .claims(Map.of("rolId",rolId))
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Se recibe el parametro 'token' para validarlo y saber si es verdadero o ha sido modificado.
     * @param token
     * @return un dato booleano 'true' o 'false' dependiendo de que el token se encuentre como validado o no.
     */
    public Boolean isTokenValid(String token) {
        try{
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch(JwtException e) {
            e.printStackTrace();
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Se entrega el un String 'token' para poder extraer sus 'claims' del 'payload'.
     * @param <T>
     * @param token
     * @param resolver
     * @return 'claims' del 'payload' en diferentes formatos de datos.
     */
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    /**
     * Método que extrae el nombre del usuario del token que se pasa como parametro.
     * @param token
     * @return un String como nombre del usuario del token.
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Método que extrae el id del usuario a quien pertenece el token.
     * @param token
     * @return un Long como id de usaurio.
     */
    public Long extractUserId(String token) {
        return  extractClaims(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Método que extrae el id del rol del usuario a quien pertenece el token.
     * @param token
     * @return un Long del rol de usaurio.
     */
    public Long extractRolId(String token) {
        return  extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    /**
     * Método para que un usuario pueda refrescar su token si sigue activo en la aplicación.
     * @param token
     * @return un String de token JWT.
     * @throws Exception de posibles errores en el proceso de refrescar el token.
     */
    public String refreshToken(String token) throws Exception {
        Claims claims;
        
        try {
            claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch(ExpiredJwtException e) {
            e.printStackTrace();
            throw new Exception("El token expiró " + e.getMessage());
        } catch(JwtException e) {
            e.printStackTrace();
            throw new Exception("El token está invalido " + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Hubo un error al validar el token " + e.getMessage());
        }

        return generateToken(claims.get("userId", Long.class), claims.getSubject(), claims.get("rolId", Long.class));
    }
}