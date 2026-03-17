package ru.leyman.loco.locoback.service.security.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.leyman.loco.locoback.service.UserService;

public class VkJwtAuthenticationConverter extends AbstractJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    public VkJwtAuthenticationConverter(UserService userService) {
        super(userService);
    }



}
