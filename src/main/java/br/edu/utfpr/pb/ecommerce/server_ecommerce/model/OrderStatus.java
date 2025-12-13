package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_order_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE tb_order_status SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class OrderStatus extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
