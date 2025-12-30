package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_order_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus extends BaseSoftDeleteEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
