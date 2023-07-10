package com.example.oauth2websocket.auth;

import com.example.oauth2websocket.auth.JwtService;
import com.example.oauth2websocket.service.IRedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class LogoutService implements LogoutHandler {
    private IRedisService iRedisService;

    private JwtService jwtService;

    @Value("${project.auth.token.prefix}")
    private String tokenPrefix;

    public LogoutService(IRedisService iRedisService, JwtService jwtService) {
        this.iRedisService = iRedisService;
        this.jwtService = jwtService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader != null && authHeader.startsWith(tokenPrefix)) {
            jwt = authHeader.substring(tokenPrefix.length());
            long expirationTime = jwtService.getJwtExpirationSecond(jwt);
            iRedisService.setValue(jwt, jwt, expirationTime);
        }
    }
}
