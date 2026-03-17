package ru.leyman.loco.locoback.service.security.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import ru.leyman.loco.locoback.domain.entity.User;
import ru.leyman.loco.locoback.domain.enums.Locale;
import ru.leyman.loco.locoback.service.UserService;

@RequiredArgsConstructor
public abstract class AbstractJwtAuthenticationConverter {

    protected final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();
    protected final UserService userService;

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
