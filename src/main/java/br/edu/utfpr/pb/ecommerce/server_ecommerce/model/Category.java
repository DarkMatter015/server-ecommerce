package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tb_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity {

    @NotBlank
    @Size(min = 4, max = 255)
    private String name;
}
