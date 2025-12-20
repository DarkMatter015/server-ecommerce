package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto.APIResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ForgetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ResetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPasswordResetToken.IPasswordResetTokenService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Auth", description = "Endpoints for authentication and password management")
public class AuthController {

    private final AuthService authService;
    private final IUserRequestService userRequestService;
    private final IPasswordResetTokenService passwordResetTokenService;

    @Operation(summary = "Validate token", description = "Validates the authenticated user's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid token"),
            @ApiResponse(responseCode = "403", description = "Invalid or expired token")
    })
    @GetMapping("/validate")
    public ResponseEntity<AuthenticationResponseDTO> validateToken() {
        return ResponseEntity.ok(authService.validateUserToken());
    }

    @Operation(summary = "Change password", description = "Changes the authenticated user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/change-password")
    public ResponseEntity<APIResponseDTO> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        userRequestService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(new APIResponseDTO("Password changed with success!"));
    }

    @Operation(summary = "Validate reset token", description = "Validates the token sent for password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid token"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    @GetMapping("/validate-reset-token")
    public ResponseEntity<APIResponseDTO> validateToken(@Parameter(description = "Reset token") @RequestParam String token) {
        passwordResetTokenService.validatePasswordResetToken(token);
            return ResponseEntity.ok(new APIResponseDTO("Valid Token."));
    }

    @Operation(summary = "Forgot password", description = "Requests an email for password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request received successfully")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponseDTO> forgetPassword(@RequestBody @Valid ForgetPasswordDTO forgetPasswordDTO) {
        passwordResetTokenService.createPasswordResetTokenForEmail(forgetPasswordDTO);
        return ResponseEntity.ok(new APIResponseDTO("If the email address exists in our database, we will send a link."));
    }

    @Operation(summary = "Reset password", description = "Resets the user's password using the token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or expired token")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<APIResponseDTO> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        passwordResetTokenService.changeUserPassword(resetPasswordDTO);
        return ResponseEntity.ok(new APIResponseDTO("Password recovery with success!"));
    }
}
