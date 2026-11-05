package org.azotiques.sonantv1.auth;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.UserApi;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserApi userApi;

    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        userApi.provision(jwt);

        return jwtAuthenticationConverter.convert(jwt);
    }
}
