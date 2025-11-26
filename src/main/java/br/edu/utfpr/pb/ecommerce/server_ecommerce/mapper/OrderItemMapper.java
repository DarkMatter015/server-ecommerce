package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ModelMapper modelMapper;

    public OrderItemResponseDTO toDTO(OrderItem item) {
        if (item == null) {
            return null;
        }

        OrderItemResponseDTO dto = new OrderItemResponseDTO();

        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());

        dto.setTotalPrice(item.getTotalPrice());

        if (item.getOrder() != null) {
            dto.setOrderId(item.getOrder().getId());
        }

        if (item.getProduct() != null) {
            dto.setProduct(map(item.getProduct(), ProductResponseDTO.class, modelMapper));
        }

        return dto;
    }

    public List<OrderItemResponseDTO> toDTOList(List<OrderItem> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO> toRequestDTOList(List<OrderItem> items) {
        return items.stream()
                .map(item -> new br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO(null, item.getProduct().getId(), item.getQuantity()))
                .collect(Collectors.toList());
    }
}
