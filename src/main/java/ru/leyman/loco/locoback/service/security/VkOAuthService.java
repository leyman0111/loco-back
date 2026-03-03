package ru.leyman.loco.locoback.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.leyman.loco.locoback.domain.dto.auth.AuthServer;
import ru.leyman.loco.locoback.domain.dto.auth.VkUserInfo;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class VkOAuthService extends AbstractOAuthService implements OAuthService {

    @Value("${vk.redirect-uri}")
    private String redirectUri;
    @Value("${vk.client-secret}")
    public String clientSecret;
    @Value("${vk.client-id}")
    private String clientId;
    @Value("${vk.info-url}")
    private String infoUrl;
    @Value("${vk.issuer}")
    private String issuer;

    public VkOAuthService(RestTemplate authClient, ObjectMapper objectMapper,
                          @Value("${vk.exchange-url}") String exchangeUrl) {
        super(authClient, objectMapper, exchangeUrl);
    }

    @Override
    public AuthServer authServer() {
        return AuthServer.VK;
    }

    @Override
    public MultiValueMap<String, String> body(Map<String, String> params) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);
        body.add("client_id", clientId);
        body.add("state", UUID.randomUUID().toString());
        body.add("code", params.get("code"));
        body.add("code_verifier", params.get("codeVerifier"));
        body.add("device_id", params.get("deviceId"));
        return body;
    }

    @Override
    public String exchangeForJwtUserInfo(String accessToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("access_token", accessToken);
        var headers = headers();
        var request = new HttpEntity<>(body, headers);
        ResponseEntity<VkUserInfo> response = authClient.exchange(
                infoUrl, HttpMethod.POST, request, VkUserInfo.class);
        var userInfo = response.getBody();
        var key = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));
        var login = userInfo.email().split("@")[0];
        return Jwts.builder()
                .subject(userInfo.user_id())
                .issuer(issuer)
                .claims(Map.of("uid", userInfo.user_id(),
                        "login", login,
                        "email", userInfo.email(),
                        "name", userInfo.first_name()))
                .signWith(key)
                .compact();
    }
}
