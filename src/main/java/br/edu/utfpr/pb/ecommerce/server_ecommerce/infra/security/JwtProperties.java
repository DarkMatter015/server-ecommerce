package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public static final String TOKEN_PREFIX = "Bearer "; // tipo da autenticação

    public static final String HEADER_STRING = "Authorization"; // header que será passado ao server com o token

}
