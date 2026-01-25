package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.role;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoleResponseDTO extends BaseResponseDTO {

    private String name;
}
