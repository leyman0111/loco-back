package ru.leyman.loco.locoback.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import ru.leyman.loco.locoback.domain.entity.User;
import ru.leyman.loco.locoback.domain.enums.Locale;
import ru.leyman.loco.locoback.service.UserService;

@Component
@RequiredArgsConstructor
public class YandexJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();
    private final UserService userService;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var authorities = defaultGrantedAuthoritiesConverter.convert(source);
        var user = this.findOrCreate(source);
        return new JwtAuthenticationToken(source, authorities, user.getEmail());
    }

    private User findOrCreate(Jwt jwt) {
        var email = jwt.getClaimAsString("email");
        var user = userService.findByEmail(email).orElseGet(() -> {
            var externalId = jwt.getClaimAsString("uid");
            var username = jwt.getClaimAsString("login");
            var name = jwt.getClaimAsString("name");
            return new User(externalId, username, name, email, Locale.RU);
        });
        return userService.save(user);

    }

}
