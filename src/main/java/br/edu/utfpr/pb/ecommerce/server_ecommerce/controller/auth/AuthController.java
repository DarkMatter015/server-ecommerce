package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto.APIResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ForgetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ResetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPasswordResetToken.IPasswordResetTokenService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserRequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class AuthController {

    private final AuthService authService;
    private final IUserRequestService userRequestService;
    private final IPasswordResetTokenService passwordResetTokenService;

    @GetMapping("/validate")
    public ResponseEntity<AuthenticationResponseDTO> validateToken() {
        return ResponseEntity.ok(authService.validateUserToken());
    }

    @PostMapping("/change-password")
    public ResponseEntity<APIResponseDTO> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        userRequestService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(new APIResponseDTO("Password changed with success!"));
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<APIResponseDTO> validateToken(@RequestParam String token) {
        passwordResetTokenService.validatePasswordResetToken(token);
            return ResponseEntity.ok(new APIResponseDTO("Valid Token."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponseDTO> forgetPassword(@RequestBody @Valid ForgetPasswordDTO forgetPasswordDTO) {
        passwordResetTokenService.createPasswordResetTokenForEmail(forgetPasswordDTO);
        return ResponseEntity.ok(new APIResponseDTO("If the email address exists in our database, we will send a link."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponseDTO> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        passwordResetTokenService.changeUserPassword(resetPasswordDTO);
        return ResponseEntity.ok(new APIResponseDTO("Password recovery with success!"));
    }
}
