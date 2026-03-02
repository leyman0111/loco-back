package ru.leyman.loco.locoback.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class YandexOAuthService implements OAuthService {

    private final RestTemplate yandexExchangeClient;
    private final ObjectMapper objectMapper;

    @Value("${yandex.exchange-url}")
    private String exchangeUrl;
    @Value("${yandex.client-secret}")
    private String yandexClientSecret;
    @Value("${yandex.client-id}")
    private String yandexClientId;
    @Value("${yandex.info-url}")
    private String infoUrl;

    @Override
    public String exchangeForAccessToken(String authCode) {
        var body = body(authCode);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        var request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = yandexExchangeClient.exchange(
                exchangeUrl, HttpMethod.POST, request, String.class);
        var authResponse = response.getBody();
        var accessToken = objectMapper.readTree(authResponse).get("access_token").asString();
        return exchangeForUserInfo(accessToken);
    }

    private MultiValueMap<String, String> body(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", authCode);
        params.add("client_id", yandexClientId);
        params.add("client_secret", yandexClientSecret);
        return params;
    }

    private String exchangeForUserInfo(String accessToken) {
        var headers = new HttpHeaders();
        headers.set("Authorization", "OAuth " + accessToken);
        var response = yandexExchangeClient.exchange(infoUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class);
        return response.getBody();
    }

}
