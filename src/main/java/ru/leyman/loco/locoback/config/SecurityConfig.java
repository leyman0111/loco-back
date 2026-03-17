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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import ru.leyman.loco.locoback.domain.enums.AuthServer;
import ru.leyman.loco.locoback.service.UserService;
import ru.leyman.loco.locoback.service.security.*;
import ru.leyman.loco.locoback.service.security.converters.LocoJwtAuthenticationConverter;
import ru.leyman.loco.locoback.service.security.converters.VkJwtAuthenticationConverter;
import ru.leyman.loco.locoback.service.security.converters.YandexJwtAuthenticationConverter;

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
    private String yandexIssuer;
    @Value("${vk.client-secret}")
    private String vkClientSecret;
    @Value("${vk.issuer-uri}")
    private String vkIssuer;
    @Value("${loco.client-secret}")
    private String locoClientSecret;
    @Value("${loco.issuer-uri}")
    private String locoIssuer;

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/auth/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(auth2 ->
                        auth2.authenticationManagerResolver(authenticationManagerResolver()))
                .build();
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
        Map<String, AuthenticationManager> authManagers = Map.of(
                yandexIssuer, yandexJwtAuthProvider()::authenticate,
                vkIssuer, vkJwtAuthProvider()::authenticate,
                locoIssuer, locoJwtAuthProvider()::authenticate);
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
        var provider = new JwtAuthenticationProvider(vkJwtDecoder());
        provider.setJwtAuthenticationConverter(new VkJwtAuthenticationConverter(userService));
        return provider;
    }

    private JwtAuthenticationProvider locoJwtAuthProvider() {
        var provider = new JwtAuthenticationProvider(locoJwtDecoder());
        provider.setJwtAuthenticationConverter(new LocoJwtAuthenticationConverter(userService));
        return provider;
    }

    private JwtDecoder vkJwtDecoder() {
        var key = Keys.hmacShaKeyFor(vkClientSecret.getBytes(StandardCharsets.UTF_8));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    private JwtDecoder locoJwtDecoder() {
        var key = Keys.hmacShaKeyFor(locoClientSecret.getBytes(StandardCharsets.UTF_8));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public Map<AuthServer, OAuthService> oAuthServices(List<OAuthService> oAuthServices) {
        return oAuthServices.stream().collect(Collectors.toMap(OAuthService::authServer, Function.identity()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
