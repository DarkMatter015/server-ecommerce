package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.filter;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        //Recuperar o token do Header(cabeçalho) da requisição
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        //Verifica se o token existe no cabeçalho
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(token);

            String username = jwt.getSubject();

            if (username != null) {
                // Extrai a claim de roles. Se a claim não existir ou for nula, retorna uma lista vazia.
                List<String> roles = jwt.getClaim("roles").asList(String.class) != null ? jwt.getClaim("roles").asList(String.class) : Collections.emptyList();
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
            return null;
        } catch (JWTVerificationException e) {
            log.warn("JWT Verification Failed: {} - Token: {}", e.getMessage(), token);
            return null;
        }

    }
}