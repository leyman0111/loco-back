package ru.leyman.loco.locoback.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.dto.AuthRequest;
import ru.leyman.loco.locoback.domain.dto.AuthResponse;
import ru.leyman.loco.locoback.domain.entity.User;
import ru.leyman.loco.locoback.domain.errors.UnauthorizedException;
import ru.leyman.loco.locoback.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocoAuthorizeService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${loco.client-secret}")
    private String clientSecret;
    @Value("${loco.issuer-uri}")
    private String issuer;

    public AuthResponse authorize(AuthRequest authRequest) {
        return userService.findByEmail(authRequest.username())
                .or(() -> {
                    log.info("User not found by email, trying found by username: {}",
                            authRequest.username());
                    return userService.findByUsername(authRequest.username());
                })
                .filter(user -> passwordEncoder.matches(authRequest.password(), user.getPassword()))
                .map(this::getJwtUserInfo)
                .map(AuthResponse::new)
                .orElseThrow(UnauthorizedException::new);
    }

    private String getJwtUserInfo(User user) {
        try {
            var key = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));
            var token = Jwts.builder()
                    .subject(String.valueOf(user.getId()))
                    .issuer(issuer)
                    .claims(Map.of("uid", user.getId(),
                            "login", user.getUsername(),
                            "email", user.getEmail(),
                            "name", user.getName()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
            log.info("Authorized user: {}", user.getUsername());
            return token;
        }  catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UnauthorizedException();
        }
    }

}
