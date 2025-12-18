package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.handler;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.exception.JsonAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", Instant.now().toString());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());


        if (exception instanceof JsonAuthenticationException) {
            errorDetails.put("error", "Invalid Request");
            errorDetails.put("message", exception.getMessage());
        } else {
            errorDetails.put("error", "Authentication Failed");
            errorDetails.put("message", "User or Password invalid.");
        }
        errorDetails.put("path", request.getRequestURI());

        response.getWriter().write(
                objectMapper.writeValueAsString(errorDetails)
        );
    }
}