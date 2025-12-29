package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseUpdateDTO;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateDTO extends BaseUpdateDTO {

    @Size(min = 1, max = 3)
    private String number;

    private String complement;
}
