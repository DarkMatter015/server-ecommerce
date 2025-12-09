package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDTO {

    @Size(min = 4, max = 255)
    private String name;
}
