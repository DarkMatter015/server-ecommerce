package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.auth.iAuthController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.auth.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ForgetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ResetPasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "Auth", description = "Endpoints for authentication and password management")
public interface IAuthController {

    @Operation(summary = "Validate token", description = "Validates the authenticated user's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid token"),
            @ApiResponse(responseCode = "403", description = "Invalid or expired token")
    })
    @GetMapping("/validate")
    ResponseEntity<AuthenticationResponseDTO> validateToken();

    @Operation(summary = "Change password", description = "Changes the authenticated user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/change-password")
    ResponseEntity<Void> changePassword(ChangePasswordDTO changePasswordDTO);

    @Operation(summary = "Validate reset token", description = "Validates the token sent for password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Valid token"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    @GetMapping("/validate-reset-token")
    ResponseEntity<Void> validateResetToken(String token);

    @Operation(summary = "Forgot password", description = "Requests an email for password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request received successfully")
    })
    @PostMapping("/forgot-password")
    ResponseEntity<Void> forgetPassword(ForgetPasswordDTO forgetPasswordDTO);

    @Operation(summary = "Reset password", description = "Resets the user's password using the token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or expired token")
    })
    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(ResetPasswordDTO resetPasswordDTO);
}
