package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderDocument;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface OrderDocumentRepository extends BaseSoftDeleteRepository<OrderDocument, Long> {

    @EntityGraph(attributePaths = {"order", "uploadedBy"})
    List<OrderDocument> findByOrderIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long orderId);

    @EntityGraph(attributePaths = {"order", "uploadedBy"})
    Optional<OrderDocument> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByOrderIdAndDocumentTypeAndDeletedAtIsNull(Long orderId, DocumentType documentType);
}
