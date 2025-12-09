package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Size(min = 3, max = 255, message = "{field.displayname.size}")
    private String displayName;

    @Size(min = 4, max = 255)
    @Email
    private String email;

    @Size(min = 6, message = "{field.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{field.password.pattern}")
    private String password;

    @Pattern(regexp = "\\d{11}", message = "{field.cpf.pattern}")
    private String cpf;

    private Set<String> roles;
}
