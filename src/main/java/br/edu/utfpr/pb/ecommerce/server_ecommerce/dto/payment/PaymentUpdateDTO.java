package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseDTO;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDTO extends BaseDTO {

    @Size(min = 4, max = 255)
    private String name;
}
