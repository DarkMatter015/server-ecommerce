package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.role.RoleResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserResponseDTO(User user) {
        super.setId(user.getId());
        super.setActive(user.isActive());
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.roles = user.getAuthorities().stream()
                .map(authority -> new RoleResponseDTO(authority.getAuthority()))
                .collect(Collectors.toList());
    }
}
