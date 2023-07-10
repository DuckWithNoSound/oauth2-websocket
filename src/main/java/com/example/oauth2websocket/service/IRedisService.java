package com.example.oauth2websocket.service;

import java.util.Map;

public interface IRedisService {

    void setValue(String key, String value, long expirationTime);
    String getValue(String key);

    boolean hasKey(String key);
}
