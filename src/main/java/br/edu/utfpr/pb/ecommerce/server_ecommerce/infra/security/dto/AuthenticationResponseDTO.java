package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto;

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
    private SecurityUserResponseDTO user;

}
