package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseResponseDTO implements Identifiable<Long> {

    private Long id;

    private Boolean active;
}
