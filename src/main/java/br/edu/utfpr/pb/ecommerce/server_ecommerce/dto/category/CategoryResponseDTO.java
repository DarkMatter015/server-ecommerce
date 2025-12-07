package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO extends BaseDTO {

    private Long id;

    private String name;
}
