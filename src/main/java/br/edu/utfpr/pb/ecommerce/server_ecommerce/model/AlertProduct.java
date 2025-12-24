package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseIdEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Ownable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_alert_product", indexes = {
        @Index(name = "idx_id_produto", columnList = "product_id"),
        @Index(name = "idx_email", columnList = "email")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertProduct extends BaseIdEntity implements Ownable {

    @NotNull
    @Email
    @Size(min = 4, max = 255)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @CreationTimestamp
    private LocalDateTime requestDate;

    @UpdateTimestamp
    private LocalDateTime processedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AlertProductStatus status = AlertProductStatus.PENDING;
}