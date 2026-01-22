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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final TranslationService translator;
    private final LocaleResolver localeResolver;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.warn("Access denied for user: {}", request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String message = translator.getMessageLocale("access.denied", localeResolver.resolveLocale(request));

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI()
        );

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
