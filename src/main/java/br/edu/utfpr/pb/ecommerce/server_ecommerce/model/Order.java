package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment.EmbeddedShipmentDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Ownable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseSoftDeleteEntity implements Ownable {


    @NotNull
    private final LocalDateTime data = LocalDateTime.now();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    @Embedded
    private EmbeddedAddress address;

    @Embedded
    private EmbeddedShipmentDTO shipment;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    public void addItem(OrderItem item){
        item.setOrder(this);
        this.orderItems.add(item);
    }
}
