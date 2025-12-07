package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tb_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Size(min = 3, max = 255)
    private String street;

    @NotBlank
    private String number;

    private String complement;

    @NotBlank
    private String neighborhood;

    @NotBlank
    @Size(min = 3, max = 255)
    private String city;

    @NotBlank
    @Size(min = 2, max = 255)
    private String state;

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "{field.cep.pattern}")
    private String cep;
}
