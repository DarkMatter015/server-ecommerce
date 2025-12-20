package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.ReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrder.IOrderResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order Read", description = "Endpoints for reading orders")
public class ReadOrderController extends ReadController<Order, OrderResponseDTO, Long> {
    private final OrderMapper orderMapper;

    public ReadOrderController(IOrderResponseService orderResponseService, ModelMapper modelMapper, OrderMapper orderMapper) {
        super(OrderResponseDTO.class, orderResponseService, modelMapper);

        this.orderMapper = orderMapper;
    }

    @Override
    protected OrderResponseDTO convertToDto(Order entity) {
        return this.orderMapper.toDTO(entity);
    }
}
