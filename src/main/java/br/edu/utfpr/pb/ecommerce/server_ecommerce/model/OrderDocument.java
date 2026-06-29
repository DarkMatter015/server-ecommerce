package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_order_document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDocument extends BaseSoftDeleteEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @Column(name = "original_name")
    private String originalName;

    @NotNull
    @Column(name = "content_type")
    private String contentType;

    @NotNull
    @Column(name = "size_bytes")
    private Long sizeBytes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @NotNull
    @Column(name = "object_key")
    private String objectKey;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
