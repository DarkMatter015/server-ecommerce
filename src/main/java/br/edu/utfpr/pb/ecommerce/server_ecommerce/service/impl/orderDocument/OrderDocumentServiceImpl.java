package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderDocument;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderDocumentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderDocument;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderDocumentRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.email.EmailService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAuthenticatedAndAdmin;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.findAndValidateOrder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDocumentServiceImpl implements IOrderDocumentService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("application/pdf", "image/jpeg", "image/png");

    private final OrderDocumentRepository orderDocumentRepository;
    private final OrderRepository orderRepository;
    private final StorageService storageService;
    private final AuthService authService;
    private final EmailService emailService;

    @Override
    @Transactional
    public OrderDocumentResponseDTO upload(Long orderId, MultipartFile file, DocumentType documentType) {
        Order order = resolveOrder(orderId);
        validateFile(file);

        User uploader = authService.getAuthenticatedUser();
        String objectKey = buildObjectKey(orderId, file.getOriginalFilename());

        try {
            storageService.upload(objectKey, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to read uploaded file for Order ID {}: {}", orderId, e.getMessage());
            throw new BusinessException(ErrorCode.DOCUMENT_STORAGE_ERROR);
        }

        OrderDocument document = OrderDocument.builder()
                .order(order)
                .originalName(StringUtils.cleanPath(
                        file.getOriginalFilename() != null ? file.getOriginalFilename() : objectKey))
                .contentType(file.getContentType())
                .sizeBytes(file.getSize())
                .documentType(documentType)
                .objectKey(objectKey)
                .uploadedBy(uploader)
                .build();

        OrderDocument saved = orderDocumentRepository.save(document);
        log.info("Document ID {} ({}) attached to Order ID {} by user ID {}",
                saved.getId(), documentType, orderId, uploader.getId());

        User customer = order.getUser();
        String documentName = saved.getOriginalName();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    emailService.sendOrderDocumentEmail(customer, orderId, documentName, documentType);
                    log.info("Order Document Email sent for Order ID: {}", orderId);
                } catch (Exception e) {
                    log.error("Error sending Order Document Email for Order ID: {}", orderId, e);
                }
            }
        });

        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDocumentResponseDTO> listByOrder(Long orderId) {
        resolveOrder(orderId);
        return orderDocumentRepository
                .findByOrderIdAndDeletedAtIsNullOrderByCreatedAtDesc(orderId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDocumentDownload download(Long orderId, Long documentId) {
        resolveOrder(orderId);
        OrderDocument document = findDocument(orderId, documentId);
        return new OrderDocumentDownload(
                storageService.download(document.getObjectKey()),
                document.getOriginalName(),
                document.getContentType(),
                document.getSizeBytes());
    }

    @Override
    @Transactional
    public void delete(Long orderId, Long documentId) {
        resolveOrder(orderId);
        OrderDocument document = findDocument(orderId, documentId);

        storageService.remove(document.getObjectKey());
        orderDocumentRepository.softDeleteById(document.getId());
        log.info("Document ID {} removed from Order ID {}", documentId, orderId);
    }

    private Order resolveOrder(Long orderId) {
        if (isAuthenticatedAndAdmin()) {
            return orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException(Order.class, orderId));
        }
        User user = authService.getAuthenticatedUser();
        return findAndValidateOrder(orderId, user, orderRepository);
    }

    private OrderDocument findDocument(Long orderId, Long documentId) {
        OrderDocument document = orderDocumentRepository.findByIdAndDeletedAtIsNull(documentId)
                .orElseThrow(() -> new ResourceNotFoundException(OrderDocument.class, documentId));
        if (!document.getOrder().getId().equals(orderId)) {
            throw new ResourceNotFoundException(OrderDocument.class, documentId);
        }
        return document;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.DOCUMENT_EMPTY);
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(ErrorCode.DOCUMENT_TYPE_NOT_ALLOWED);
        }
    }

    private String buildObjectKey(Long orderId, String originalFilename) {
        String extension = "";
        if (originalFilename != null) {
            String cleaned = StringUtils.cleanPath(originalFilename);
            String ext = StringUtils.getFilenameExtension(cleaned);
            if (ext != null && !ext.isBlank()) {
                extension = "." + ext.toLowerCase();
            }
        }
        return "orders/" + orderId + "/" + UUID.randomUUID() + extension;
    }

    private OrderDocumentResponseDTO toDTO(OrderDocument document) {
        return OrderDocumentResponseDTO.builder()
                .id(document.getId())
                .orderId(document.getOrder().getId())
                .originalName(document.getOriginalName())
                .contentType(document.getContentType())
                .sizeBytes(document.getSizeBytes())
                .documentType(document.getDocumentType())
                .uploadedByName(document.getUploadedBy() != null
                        ? document.getUploadedBy().getDisplayName() : null)
                .createdAt(document.getCreatedAt())
                .build();
    }
}
