package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderDocument;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderDocumentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOrderDocumentService {

    OrderDocumentResponseDTO upload(Long orderId, MultipartFile file, DocumentType documentType);

    List<OrderDocumentResponseDTO> listByOrder(Long orderId);

    OrderDocumentDownload download(Long orderId, Long documentId);

    void delete(Long orderId, Long documentId);
}
