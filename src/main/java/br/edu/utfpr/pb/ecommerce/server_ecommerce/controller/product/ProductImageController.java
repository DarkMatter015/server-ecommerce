package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.product;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductImageResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.productImage.IProductImageService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.productImage.ProductImageDownload;
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
@RequestMapping("products/{productId}/images")
@RequiredArgsConstructor
@Tag(name = "Product Images", description = "Endpoints for managing product images stored in MinIO")
public class ProductImageController {

    private final IProductImageService productImageService;

    @Operation(summary = "Upload product images",
            description = "Uploads one or more images for a product (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Images uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file type or empty file"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @SecurityRequirement(name = "bearer-key")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProductImageResponseDTO>> upload(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @RequestParam("files") MultipartFile[] files) {
        List<ProductImageResponseDTO> images = productImageService.upload(productId, files);
        return ResponseEntity.status(201).body(images);
    }

    @Operation(summary = "List product images",
            description = "Lists all images attached to a product")
    @ApiResponse(responseCode = "200", description = "Images listed successfully")
    @GetMapping
    public ResponseEntity<List<ProductImageResponseDTO>> list(
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.listByProduct(productId));
    }

    @Operation(summary = "Get product image",
            description = "Streams a product image inline (public)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image returned successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @GetMapping("{imageId}")
    public ResponseEntity<Resource> get(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Image ID") @PathVariable Long imageId) {
        ProductImageDownload image = productImageService.download(productId, imageId);
        Resource resource = new InputStreamResource(image.stream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + image.filename() + "\"")
                .contentType(MediaType.parseMediaType(image.contentType()))
                .contentLength(image.size())
                .body(resource);
    }

    @Operation(summary = "Delete product image",
            description = "Removes an image attached to a product (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("{imageId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Image ID") @PathVariable Long imageId) {
        productImageService.delete(productId, imageId);
        return ResponseEntity.noContent().build();
    }
}
