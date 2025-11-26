package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull
    private List<OrderItemRequestDTO> orderItems;

    @NotNull
    @Valid
    private AddressRequestDTO address;

    @NotNull
    private Long paymentId;

    @NotNull
    private Long shipmentId;
}
