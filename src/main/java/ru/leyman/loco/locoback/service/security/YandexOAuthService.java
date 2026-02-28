package ru.leyman.loco.locoback.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class YandexOAuthService implements OAuthService {

    private final RestTemplate yandexExchangeClient;

    @Value("${yandex.exchange-url}")
    private String exchangeUrl;

    @Override
    public String exchangeForAccessToken(String authCode) {
        var headers = new HttpHeaders();
        headers.set("Authorization", "OAuth " + authCode);
        var response = yandexExchangeClient.exchange(exchangeUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class, Map.of("format", "jwt"));
        return response.getBody();
    }

}
