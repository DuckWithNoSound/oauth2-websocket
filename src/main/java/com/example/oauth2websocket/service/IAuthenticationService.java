package com.example.oauth2websocket.service;

import com.example.oauth2websocket.common.Constants;
import com.example.oauth2websocket.dto.AuthenticationDto;
import com.example.oauth2websocket.entity.Role;
import com.example.oauth2websocket.entity.UserEntity;
import com.example.oauth2websocket.exception.KonfnikException;

public interface IAuthenticationService {

    String register(AuthenticationDto authenticationDto) throws KonfnikException;

    String authenticate(AuthenticationDto authenticationDto) throws KonfnikException;

}
