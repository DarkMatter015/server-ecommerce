package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.filter;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.JwtProperties;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.auth.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.auth.LoginRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.exception.JsonAuthenticationException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.TranslationService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductRequestService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final TranslationService translationService;
    private final LocaleResolver localeResolver;
    private final JwtProperties jwtProperties;
    private final IAlertProductRequestService alertProductRequestService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthService authService,
                                   ObjectMapper objectMapper,
                                   JwtProperties jwtProperties,
                                   AuthenticationFailureHandler authenticationFailureHandler,
                                   IAlertProductRequestService alertProductRequestService,
                                   TranslationService translationService,
                                   LocaleResolver localeResolver) {
        super(authenticationManager);
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.translationService = translationService;
        this.jwtProperties = jwtProperties;
        this.localeResolver = localeResolver;
        this.alertProductRequestService = alertProductRequestService;

        this.setAuthenticationFailureHandler(authenticationFailureHandler);
        this.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String appSource = request.getHeader(JwtProperties.HEADER_ORIGIN);
            LoginRequestDTO credentials = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(),
                    credentials.getPassword());

            Authentication authResult = super.getAuthenticationManager().authenticate(authToken);

            if ("admin".equalsIgnoreCase(appSource)) {
                boolean isAdmin = authResult.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ADMIN"));

                if (!isAdmin) {
                    throw new BadCredentialsException(translationService.getMessageLocale("access.denied", localeResolver.resolveLocale(request)));
                }
            }

            return authResult;
        } catch (IOException e) {
            throw new JsonAuthenticationException("Invalid data format for login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User) authService.loadUserByUsername(authResult.getName());

        alertProductRequestService.syncOrphanAlerts(user);

        String token = JWT.create()
                .withSubject(user.getId().toString())
                .withExpiresAt(
                        getExpirationDate()
                )
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new AuthenticationResponseDTO(token, new UserResponseDTO(user), getExpirationDate().toEpochMilli())
                )
        );
    }

    private Instant getExpirationDate() {
        return Instant.now().plusMillis(jwtProperties.getExpirationTime());
    }
}