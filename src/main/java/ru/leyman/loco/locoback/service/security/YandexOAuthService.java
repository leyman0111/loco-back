package ru.leyman.loco.locoback.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.leyman.loco.locoback.domain.enums.AuthServer;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Service
public class YandexOAuthService extends AbstractOAuthService implements OAuthService{

    @Value("${yandex.client-secret}")
    private String clientSecret;
    @Value("${yandex.client-id}")
    private String clientId;
    @Value("${yandex.info-url}")
    private String infoUrl;

    public YandexOAuthService(RestTemplate authClient, ObjectMapper objectMapper,
                              @Value("${yandex.exchange-url}") String exchangeUrl) {
        super(authClient, objectMapper, exchangeUrl);
    }

    @Override
    public AuthServer authServer() {
        return AuthServer.YANDEX;
    }

    @Override
    public MultiValueMap<String, String> body(Map<String, String> params) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", params.get("authCode"));
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        return body;
    }

    @Override
    public String getJwtUserInfo(String exchangeResponseBody) {
        var accessToken = objectMapper.readTree(exchangeResponseBody).get("access_token").asString();
        var headers = new HttpHeaders();
        headers.set("Authorization", "OAuth " + accessToken);
        log.info("Trying to exchange for userInfo");
        var response = authClient.exchange(infoUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class);
        return response.getBody();
    }

}
