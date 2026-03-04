package ru.leyman.loco.locoback.config;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import ru.leyman.loco.locoback.domain.enums.AuthServer;
import ru.leyman.loco.locoback.service.UserService;
import ru.leyman.loco.locoback.service.security.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${yandex.client-secret}")
    private String yandexClientSecret;
    @Value("${yandex.issuer-uri}")
    private String yandexIssuerUri;
    @Value("${vk.client-secret}")
    private String vkClientSecret;
    @Value("${vk.issuer-uri}")
    private String vkIssuerUri;

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/auth/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(auth2 ->
                        auth2.authenticationManagerResolver(authenticationManagerResolver()))
                .build();
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
        Map<String, AuthenticationManager> authManagers = Map.of(
                yandexIssuerUri, yandexJwtAuthProvider()::authenticate,
                vkIssuerUri, vkJwtAuthProvider()::authenticate);
        return new IssuerAuthManagerResolver(authManagers);
    }

    private JwtAuthenticationProvider yandexJwtAuthProvider() {
        var key = Keys.hmacShaKeyFor(yandexClientSecret.getBytes(StandardCharsets.UTF_8));
        var jwtDecoder = NimbusJwtDecoder.withSecretKey(key).build();
        var provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(new YandexJwtAuthenticationConverter(userService));
        return provider;
    }

    private JwtAuthenticationProvider vkJwtAuthProvider() {
        var jwtDecoder = vkJwtDecoder();
        var provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(new VkJwtAuthenticationConverter(userService));
        return provider;
    }

    private JwtDecoder vkJwtDecoder() {
        var key = Keys.hmacShaKeyFor(vkClientSecret.getBytes(StandardCharsets.UTF_8));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public Map<AuthServer, OAuthService> oAuthServices(List<OAuthService> oAuthServices) {
        return oAuthServices.stream().collect(Collectors.toMap(OAuthService::authServer, Function.identity()));
    }

}
