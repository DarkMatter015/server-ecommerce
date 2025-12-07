package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO extends BaseDTO {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String urlImage;

    private Integer quantityAvailableInStock;

    private CategoryResponseDTO category;
}
