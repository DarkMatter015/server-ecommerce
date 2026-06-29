package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderDocumentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderDocument.IOrderDocumentService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderDocument.OrderDocumentDownload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("orders/{orderId}/documents")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order Documents", description = "Endpoints for managing order attachments (PDF/images)")
public class OrderDocumentController {

    private final IOrderDocumentService orderDocumentService;

    @Operation(summary = "Upload order document",
            description = "Attaches a PDF or image document to an order (admin or order owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file type or empty file"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderDocumentResponseDTO> upload(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") DocumentType type) {
        OrderDocumentResponseDTO dto = orderDocumentService.upload(orderId, file, type);
        return ResponseEntity.status(201).body(dto);
    }

    @Operation(summary = "List order documents",
            description = "Lists all documents attached to an order (admin or order owner)")
    @ApiResponse(responseCode = "200", description = "Documents listed successfully")
    @GetMapping
    public ResponseEntity<List<OrderDocumentResponseDTO>> list(
            @Parameter(description = "Order ID") @PathVariable Long orderId) {
        return ResponseEntity.ok(orderDocumentService.listByOrder(orderId));
    }

    @Operation(summary = "Download order document",
            description = "Downloads a document attached to an order (admin or order owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document returned successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("{documentId}/download")
    public ResponseEntity<Resource> download(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Parameter(description = "Document ID") @PathVariable Long documentId) {
        OrderDocumentDownload doc = orderDocumentService.download(orderId, documentId);
        Resource resource = new InputStreamResource(doc.stream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.filename() + "\"")
                .contentType(MediaType.parseMediaType(doc.contentType()))
                .contentLength(doc.size())
                .body(resource);
    }

    @Operation(summary = "Delete order document",
            description = "Removes a document attached to an order (admin or order owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @DeleteMapping("{documentId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Parameter(description = "Document ID") @PathVariable Long documentId) {
        orderDocumentService.delete(orderId, documentId);
        return ResponseEntity.noContent().build();
    }
}
