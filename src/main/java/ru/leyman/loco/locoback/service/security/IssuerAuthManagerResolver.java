package ru.leyman.loco.locoback.service.security;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

@Log4j2
@RequiredArgsConstructor
public class IssuerAuthManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
    private static final AuthenticationManager DO_NOTHING = auth -> auth;

    private final Map<String, AuthenticationManager> authenticationManagers;

    @Override
    public AuthenticationManager resolve(HttpServletRequest context) {
        var token = context.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        if (!hasText(token))
            return DO_NOTHING;
        var issuer = getIssuer(token);
        if (!hasText(issuer)) {
            return DO_NOTHING;
        }
        return Objects.requireNonNullElse(authenticationManagers.get(issuer), DO_NOTHING);
    }

    private String getIssuer(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getIssuer();
        } catch (ParseException e) {
            log.warn("Can`t parse jwt: {}", token);
            return "VK";
        }
    }

}
