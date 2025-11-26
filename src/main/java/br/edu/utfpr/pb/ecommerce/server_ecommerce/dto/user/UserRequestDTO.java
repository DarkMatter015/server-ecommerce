package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank
    @Size(min = 4, max = 255)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 255, message = "{field.displayname.size}")
    private String displayName;

    @NotBlank
    @Size(min = 6, message = "{field.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{field.password.pattern}")
    private String password;

    @NotBlank
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
