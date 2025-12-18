package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityResponseDTO {

    private String authority;

} 