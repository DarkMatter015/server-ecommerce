package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user;

import jakarta.validation.constraints.Email;
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

    private Set<String> roles;
}
