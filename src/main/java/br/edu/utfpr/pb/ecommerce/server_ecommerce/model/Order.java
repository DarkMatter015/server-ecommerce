package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment.EmbeddedShipmentDTO;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    @Embedded
    private EmbeddedAddress address;

    @NotNull
    @Embedded
    private EmbeddedShipmentDTO shipment;

    @Enumerated(EnumType.STRING)
    private br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.OrderStatus status = br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.OrderStatus.PENDING;

    public void addItem(OrderItem item){
        item.setOrder(this);
        this.orderItems.add(item);
    }
}
