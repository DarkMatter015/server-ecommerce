package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateQuantityOfProduct;

@Entity
@Table(name = "tb_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseSoftDeleteEntity {

    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    @Column(name = "url_image")
    private String urlImage;

    @Column(name = "quantity_available_in_stock")
    @NotNull
    @Min(value = 0, message = "{field.quantity.min}")
    private Integer quantityAvailableInStock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    public void decreaseQuantity(Integer decreaseQuantity){
        validateQuantityOfProduct(decreaseQuantity, this);
        this.quantityAvailableInStock -= decreaseQuantity;
    }

    public void increaseQuantity(Integer increaseQuantity){
        this.quantityAvailableInStock += increaseQuantity;
    }

}
