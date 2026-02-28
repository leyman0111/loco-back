package ru.leyman.loco.locoback.service.security;

public interface OAuthService {

    String exchangeForAccessToken(String authCode);

}
