package com.inovaceifa.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // depois iremos mover isso para config / env
    private static final String SECRET =
            "chave-super-secreta-com-mais-de-32-caracteres-123456";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 8; // 8 horas

    private final Key secretKey =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String gerarToken(Long usuarioId, String email) {

        return Jwts.builder()
                .setSubject(email)
                .claim("usuarioId", usuarioId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailDoToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
