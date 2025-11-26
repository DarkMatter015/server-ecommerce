package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;

    private String displayName;

    private String email;

    private String cpf;
}
