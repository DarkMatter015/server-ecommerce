package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDocumentResponseDTO {
    private Long id;
    private Long orderId;
    private String originalName;
    private String contentType;
    private Long sizeBytes;
    private DocumentType documentType;
    private String uploadedByName;
    private LocalDateTime createdAt;
}
