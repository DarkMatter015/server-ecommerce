package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCustomerDTO {

    private Long id;

    private String name;

    private String email;

    private String cpf;
}
