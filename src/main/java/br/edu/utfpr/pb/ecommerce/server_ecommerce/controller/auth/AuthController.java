package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ChangePasswordRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ForgetPasswordRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ResetPasswordRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPasswordResetToken.IPasswordResetTokenService;
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
    private final IPasswordResetTokenService passwordResetTokenService;
    private final ModelMapper modelMapper;

    @GetMapping("/validate")
    public ResponseEntity<AuthenticationResponseDTO> validateToken() {
        return ResponseEntity.ok(authService.validateUserToken());
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserResponseDTO> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        return ResponseEntity.ok(modelMapper.map(authService.changePassword(changePasswordRequestDTO), UserResponseDTO.class));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgetPassword(@RequestBody @Valid ForgetPasswordRequestDTO forgetPasswordRequestDTO) {

        return ResponseEntity.ok("If the email address exists in our database, we will send a link.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO) {

        return ResponseEntity.ok("If the email address exists in our database, we will send a link.");
    }
}
