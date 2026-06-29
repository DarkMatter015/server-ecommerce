package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.productImage;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductImageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageService {

    List<ProductImageResponseDTO> upload(Long productId, MultipartFile[] files);

    List<ProductImageResponseDTO> listByProduct(Long productId);

    ProductImageDownload download(Long productId, Long imageId);

    void delete(Long productId, Long imageId);
}
