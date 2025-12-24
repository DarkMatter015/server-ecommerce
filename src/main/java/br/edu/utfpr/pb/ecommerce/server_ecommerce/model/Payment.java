package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "tb_payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE tb_payment SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Payment extends BaseSoftDeleteEntity {

    @NotBlank
    @Size(min = 4, max = 255)
    @Column(unique = true)
    private String name;

}
