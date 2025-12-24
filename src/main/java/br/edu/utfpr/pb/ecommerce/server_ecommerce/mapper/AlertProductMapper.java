package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Component
@RequiredArgsConstructor
public class AlertProductMapper {

    private final ModelMapper modelMapper;

    public AlertProduct toEntity(AlertProductRequestDTO dto, Product product, User user) {
        String targetEmail = (user != null) ? user.getEmail() : dto.getEmail();
        return AlertProduct.builder()
                .product(product)
                .user(user)
                .email(targetEmail)
                .status(AlertProductStatus.PENDING)
                .build();
    }

    public AlertProductResponseDTO toDTO(AlertProduct alert) {
        return AlertProductResponseDTO.builder()
                .id(alert.getId())
                .email(alert.getEmail())
                .user(
                        alert.getUser() != null ?
                                map(alert.getUser(), UserResponseDTO.class, modelMapper) :
                                null
                )
                .productId(alert.getProduct().getId())
                .productName(alert.getProduct().getName())
                .requestDate(alert.getRequestDate().toString())
                .processedAt(alert.getProcessedAt() != null ? alert.getProcessedAt().toString() : null)
                .status(alert.getStatus().toString())
                .build();
    }

}
