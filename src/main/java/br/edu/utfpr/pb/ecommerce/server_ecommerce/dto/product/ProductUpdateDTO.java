package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO extends BaseDTO {

    @Size(min = 2, max = 255)
    private String name;

    private String description;

    @Positive
    private BigDecimal price;

    private String urlImage;

    @Min(value = 0, message = "{field.quantity.min}")
    private Integer quantityAvailableInStock;

    private Long categoryId;
}
