package ru.leyman.loco.locoback.service.security;

import ru.leyman.loco.locoback.domain.enums.AuthServer;

import java.util.Map;

public interface OAuthService {

    AuthServer authServer();

    String exchangeForAccessToken(Map<String, String> params);

}
