package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/validate")
    public ResponseEntity<AuthenticationResponseDTO> validateToken() {
        return ResponseEntity.ok(authService.validateUserToken());
    }
}
