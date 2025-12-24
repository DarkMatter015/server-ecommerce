package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE tb_role SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Role extends BaseSoftDeleteEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
