package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusResponseDTO {
    private Long id;
    private String name;
}
