package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO extends BaseResponseDTO {

    private String name;
}
