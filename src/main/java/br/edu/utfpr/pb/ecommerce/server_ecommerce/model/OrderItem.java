package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Ownable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem extends BaseSoftDeleteEntity implements Ownable {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    @Positive(message = "{field.quantity.min}")
    private Integer quantity;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        if (quantity != null && product != null && product.getPrice() != null) {
            this.totalPrice = product.getPrice().multiply(new BigDecimal(quantity));
        }
    }

    @Override
    public User getUser() {
        return this.order.getUser();
    }
}
