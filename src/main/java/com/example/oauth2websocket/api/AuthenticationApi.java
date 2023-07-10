package com.example.oauth2websocket.api;

import com.example.oauth2websocket.dto.AuthenticationDto;
import com.example.oauth2websocket.exception.KonfnikException;
import com.example.oauth2websocket.service.IAuthenticationService;
import com.example.oauth2websocket.service.impl.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationApi {

    private final IAuthenticationService iAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthenticationDto authenticationDto) throws KonfnikException {
        return ResponseEntity.ok(iAuthenticationService.register(authenticationDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationDto authenticationDto) throws KonfnikException {
        return ResponseEntity.ok(iAuthenticationService.authenticate(authenticationDto));
    }
}
