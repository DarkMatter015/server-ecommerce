package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE tb_product SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Product extends BaseEntity {

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
        if (decreaseQuantity > this.quantityAvailableInStock)
            throw new InvalidQuantityException("Quantity greater than that available in the product stock. Quantity available in stock: " + this.quantityAvailableInStock + ".");
        this.quantityAvailableInStock -= decreaseQuantity;
    }

    public void increaseQuantity(Integer increaseQuantity){
        this.quantityAvailableInStock += increaseQuantity;
    }

}
