package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponseDTO {

    private Long id;

    private String url;

    private Integer position;
}
