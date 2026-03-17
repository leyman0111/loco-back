package ru.leyman.loco.locoback.service.security.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.leyman.loco.locoback.domain.errors.UnauthorizedException;
import ru.leyman.loco.locoback.service.UserService;

public class LocoJwtAuthenticationConverter extends AbstractJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    public LocoJwtAuthenticationConverter(UserService userService) {
        super(userService);
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var authorities = defaultGrantedAuthoritiesConverter.convert(source);
        var email = source.getClaimAsString("email");
        var user = userService.findByEmail(email).orElseThrow(UnauthorizedException::new);
        return new JwtAuthenticationToken(source, authorities, user.getEmail());
    }

}
