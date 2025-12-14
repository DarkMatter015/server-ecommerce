package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.ChangePasswordRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @GetMapping("/validate")
    public ResponseEntity<AuthenticationResponseDTO> validateToken() {
        return ResponseEntity.ok(authService.validateUserToken());
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserResponseDTO> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        return ResponseEntity.ok(modelMapper.map(authService.changePassword(changePasswordRequestDTO), UserResponseDTO.class));
    }
}
