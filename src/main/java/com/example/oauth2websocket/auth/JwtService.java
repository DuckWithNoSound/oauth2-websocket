package com.example.oauth2websocket.auth;

import com.example.oauth2websocket.common.Constants;
import com.example.oauth2websocket.service.IRedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private final IRedisService iRedisService;

    @Value("${project.auth.token.secret-key}")
    private String secretKey;

    @Value("${project.auth.token.expiration-time-minutes}")
    private String tokenExpirationTime;

    public JwtService(IRedisService iRedisService) {
        this.iRedisService = iRedisService;
    }

    public long getJwtExpirationSecond(String token) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getClaim(token, Claims::getExpiration));

        long tokenExpirationSecond = calendar.getTimeInMillis();
        long currentSecond = Calendar.getInstance().getTimeInMillis();

        return tokenExpirationSecond - currentSecond;
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return  Jwts.builder()
                .addClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(tokenExpirationTime)*60000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    private  <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaims(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] secretKeyByteArray = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyByteArray);
    }
}
