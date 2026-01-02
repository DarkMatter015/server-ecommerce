package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
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
        if (item == null) return null;
        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .orderId(item.getOrder() != null ? item.getOrder().getId() : null)
                .product(map(item.getProduct(), ProductResponseDTO.class, modelMapper))
                .build();
    }

    public List<OrderItemResponseDTO> toDTOList(List<OrderItem> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderItemRequestDTO> toRequestDTOList(List<OrderItem> items) {
        return items.stream()
                .map(item -> OrderItemRequestDTO.builder()
                        .orderId(null)
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}
