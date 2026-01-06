package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment.EmbeddedShipmentDetails;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order.OrderEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.address.EmbeddedAddress;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final ModelMapper modelMapper;

    public OrderResponseDTO toDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .data(order.getData())
                .userId(order.getUser().getId())
                .address(order.getAddress())
                .orderItems(orderItemMapper.toDTOList(order.getOrderItems()))
                .payment(map(order.getPayment(), PaymentResponseDTO.class, modelMapper))
                .shipment(order.getShipment())
                .status(order.getStatus().getName())
                .statusMessage(order.getStatusMessage())
                .build();
    }

    public Order toEntity(OrderRequestDTO dto, Payment payment) {
        return Order.builder()
                .address(map(dto.getAddress(), EmbeddedAddress.class, modelMapper))
                .payment(payment)
                .shipment(EmbeddedShipmentDetails.builder().id(dto.getShipmentId()).build())
                .build();
    }

    public OrderEventDTO toEventDTO(Order order, String cpf, String locale) {
        return OrderEventDTO.builder()
                .orderId(order.getId())
                .date(order.getData().toLocalDate())
                .payment(order.getPayment())
                .orderItems(new HashSet<>(orderItemMapper.toRequestDTOList(order.getOrderItems())))
                .address(map(order.getAddress(), AddressRequestDTO.class, modelMapper))
                .shipmentId(order.getShipment().getId())
                .userCpf(cpf)
                .locale(locale)
                .build();
    }

}
