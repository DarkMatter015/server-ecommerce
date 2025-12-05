package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_order_status")
public class OrderStatus extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
