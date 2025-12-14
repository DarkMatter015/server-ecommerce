package br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequestDTO {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6, message = "{field.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{field.password.pattern}")
    private String newPassword;

    @NotBlank
    private String confirm_password;
}
