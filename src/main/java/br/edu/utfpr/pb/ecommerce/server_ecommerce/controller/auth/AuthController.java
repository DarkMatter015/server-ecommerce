package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth.iAuthController.IAuthController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.auth.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ForgetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ResetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.PasswordResetToken.IPasswordResetToken.IPasswordResetTokenService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.IUser.IUserRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController implements IAuthController {

    private final AuthService authService;
    private final IUserRequestService userRequestService;
    private final IPasswordResetTokenService passwordResetTokenService;

    @Override
    @GetMapping("/validate")
    public ResponseEntity<AuthenticationResponseDTO> validateToken() {
        return ResponseEntity.ok(authService.validateUserToken());
    }

    @Override
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        userRequestService.changePassword(changePasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/validate-reset-token")
    public ResponseEntity<Void> validateResetToken(@RequestParam String token) {
        passwordResetTokenService.validatePasswordResetToken(token);
            return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgetPassword(@RequestBody @Valid ForgetPasswordDTO forgetPasswordDTO) {
        passwordResetTokenService.createPasswordResetTokenForEmail(forgetPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        passwordResetTokenService.changeUserPassword(resetPasswordDTO);
        return ResponseEntity.noContent().build();
    }
}
