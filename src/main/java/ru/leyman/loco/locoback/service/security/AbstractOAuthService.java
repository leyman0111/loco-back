package ru.leyman.loco.locoback.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractOAuthService {

    protected final RestTemplate authClient;
    protected final ObjectMapper objectMapper;
    protected final String exchangeUrl;

    public String exchangeForAccessToken(Map<String, String> params) {
        var body = body(params);
        var headers = headers();
        var request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = authClient.exchange(
                exchangeUrl, HttpMethod.POST, request, String.class);
        return getJwtUserInfo(response.getBody());
    }

    protected abstract MultiValueMap<String, String> body(Map<String, String> params);

    protected abstract String getJwtUserInfo(String exchangeResponseBody);

    protected HttpHeaders headers() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
