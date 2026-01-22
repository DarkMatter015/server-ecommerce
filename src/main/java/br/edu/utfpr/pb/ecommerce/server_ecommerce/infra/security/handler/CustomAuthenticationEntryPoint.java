package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.handler;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto.ApiErrorDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.TranslationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;

@Component("authenticationEntryPoint")
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final TranslationService translator;
    private final LocaleResolver localeResolver;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.warn("Unauthorized access attempt: {}", authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String message = translator.getMessageLocale("token.missing.or.invalid", localeResolver.resolveLocale(request));

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI()
        );

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
