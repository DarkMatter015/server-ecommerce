package br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseSoftDeleteEntity extends BaseIdEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isActive() {
        return deletedAt == null;
    }
}
