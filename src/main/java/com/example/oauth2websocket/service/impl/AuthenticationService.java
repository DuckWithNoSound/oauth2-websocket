package com.example.oauth2websocket.service.impl;

import com.example.oauth2websocket.auth.JwtService;
import com.example.oauth2websocket.common.Constants;
import com.example.oauth2websocket.dto.AuthenticationDto;
import com.example.oauth2websocket.entity.Role;
import com.example.oauth2websocket.entity.UserEntity;
import com.example.oauth2websocket.exception.KonfnikException;
import com.example.oauth2websocket.repository.IUserRepository;
import com.example.oauth2websocket.service.IAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final IUserRepository IUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Transactional
    public String register(AuthenticationDto authenticationDto) throws KonfnikException {
        String username = authenticationDto.getUsername();

        if(IUserRepository.findByUsername(username).isPresent()) {
            throw new KonfnikException(Constants.USERNAME_EXIST);
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(authenticationDto.getPassword()))
                .role(Role.USER)
                .status(1)
                .ins_dtm(new Timestamp(System.currentTimeMillis()))
                .build();

        IUserRepository.save(userEntity);

        return jwtService.generateToken(userEntity);
    }

    public String authenticate(AuthenticationDto authenticationDto) throws KonfnikException {
        String username = authenticationDto.getUsername();
        String password = authenticationDto.getPassword();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception exception){
            throw new KonfnikException(Constants.USERNAME_OR_PASSWORD_INCORRECT);
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .build();

        return jwtService.generateToken(userEntity);
    }
}
