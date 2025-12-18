package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.dto.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record OrderEventDTO(
        Long orderId,
        LocalDate date,
        Payment payment,
        Set<OrderItemRequestDTO> orderItems,
        AddressRequestDTO address,
        Long shipmentId,
        String userCpf
) {
}
