package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment.EmbeddedShipment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.EmbeddedAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;

    private LocalDateTime data;

    private Long userId;

    private List<OrderItemResponseDTO> orderItems;

    private EmbeddedAddress address;

    private PaymentResponseDTO payment;

    private EmbeddedShipment shipment;
}
