package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.role.RoleResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponseDTO extends BaseResponseDTO {

    private String displayName;

    private String email;

    private String cpf;

    private List<RoleResponseDTO> roles;
}
