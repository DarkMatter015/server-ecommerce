package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.productImage;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductImageResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.config.minio.MinioProperties;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.ProductImage;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductImageRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements IProductImageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final StorageService storageService;
    private final MinioProperties minioProperties;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public List<ProductImageResponseDTO> upload(Long productId, MultipartFile[] files) {
        Product product = findProduct(productId);

        if (files == null || files.length == 0) {
            throw new BusinessException(ErrorCode.PRODUCT_IMAGE_EMPTY);
        }

        int nextPosition = (int) productImageRepository.countByProductIdAndDeletedAtIsNull(productId);
        List<ProductImageResponseDTO> result = new ArrayList<>();

        for (MultipartFile file : files) {
            validateFile(file);
            String objectKey = buildObjectKey(productId, file.getOriginalFilename());

            try {
                storageService.upload(minioProperties.getProductBucket(), objectKey,
                        file.getInputStream(), file.getSize(), file.getContentType());
            } catch (BusinessException e) {
                throw new BusinessException(ErrorCode.PRODUCT_IMAGE_STORAGE_ERROR);
            } catch (Exception e) {
                log.error("Failed to read uploaded image for Product ID {}: {}", productId, e.getMessage());
                throw new BusinessException(ErrorCode.PRODUCT_IMAGE_STORAGE_ERROR);
            }

            ProductImage image = ProductImage.builder()
                    .product(product)
                    .originalName(StringUtils.cleanPath(
                            file.getOriginalFilename() != null ? file.getOriginalFilename() : objectKey))
                    .contentType(file.getContentType())
                    .sizeBytes(file.getSize())
                    .objectKey(objectKey)
                    .position(nextPosition++)
                    .build();

            ProductImage saved = productImageRepository.save(image);
            log.info("Image ID {} attached to Product ID {}", saved.getId(), productId);
            result.add(productMapper.toImageDTO(saved));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponseDTO> listByProduct(Long productId) {
        findProduct(productId);
        return productImageRepository
                .findByProductIdAndDeletedAtIsNullOrderByPositionAscIdAsc(productId)
                .stream()
                .map(productMapper::toImageDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageDownload download(Long productId, Long imageId) {
        ProductImage image = findImage(productId, imageId);
        return new ProductImageDownload(
                storageService.download(minioProperties.getProductBucket(), image.getObjectKey()),
                image.getOriginalName(),
                image.getContentType(),
                image.getSizeBytes());
    }

    @Override
    @Transactional
    public void delete(Long productId, Long imageId) {
        ProductImage image = findImage(productId, imageId);
        storageService.remove(minioProperties.getProductBucket(), image.getObjectKey());
        productImageRepository.softDeleteById(image.getId());
        log.info("Image ID {} removed from Product ID {}", imageId, productId);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, productId));
    }

    private ProductImage findImage(Long productId, Long imageId) {
        ProductImage image = productImageRepository.findByIdAndDeletedAtIsNull(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(ProductImage.class, imageId));
        if (!image.getProduct().getId().equals(productId)) {
            throw new ResourceNotFoundException(ProductImage.class, imageId);
        }
        return image;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_IMAGE_EMPTY);
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(ErrorCode.PRODUCT_IMAGE_TYPE_NOT_ALLOWED);
        }
    }

    private String buildObjectKey(Long productId, String originalFilename) {
        String extension = "";
        if (originalFilename != null) {
            String cleaned = StringUtils.cleanPath(originalFilename);
            String ext = StringUtils.getFilenameExtension(cleaned);
            if (ext != null && !ext.isBlank()) {
                extension = "." + ext.toLowerCase();
            }
        }
        return "products/" + productId + "/" + UUID.randomUUID() + extension;
    }
}
