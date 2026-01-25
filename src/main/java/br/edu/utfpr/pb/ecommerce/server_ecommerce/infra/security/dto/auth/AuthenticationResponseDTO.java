package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.auth;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponseDTO {

    private String token;
    private UserResponseDTO user;

}
