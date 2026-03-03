package ru.leyman.loco.locoback.config;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.leyman.loco.locoback.domain.dto.auth.AuthServer;
import ru.leyman.loco.locoback.service.security.OAuthService;
import ru.leyman.loco.locoback.service.security.YandexJwtAuthenticationConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final YandexJwtAuthenticationConverter yandexJwtAuthenticationConverter;

    @Value("${yandex.client-secret}")
    public String yandexClientSecret;

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth2 ->
                        auth2.jwt(jwt -> jwt.jwtAuthenticationConverter(yandexJwtAuthenticationConverter)))
                .build();
    }

    @Bean
    public JwtDecoder yandexJwtDecoder() {
        var key = Keys.hmacShaKeyFor(yandexClientSecret.getBytes(StandardCharsets.UTF_8));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public Map<AuthServer, OAuthService> oAuthServices(List<OAuthService> oAuthServices) {
        return oAuthServices.stream().collect(Collectors.toMap(OAuthService::authServer, Function.identity()));
    }

}
