package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateDTO extends BaseDTO {

    private String number;

    private String complement;
}
