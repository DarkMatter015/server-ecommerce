package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment.EmbeddedShipmentDetails;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.address.EmbeddedAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;

    private LocalDateTime data;

    private Long userId;

    private List<OrderItemResponseDTO> orderItems;

    private EmbeddedAddress address;

    private PaymentResponseDTO payment;

    private EmbeddedShipmentDetails shipment;

    private String status;

    private String statusMessage;
}
