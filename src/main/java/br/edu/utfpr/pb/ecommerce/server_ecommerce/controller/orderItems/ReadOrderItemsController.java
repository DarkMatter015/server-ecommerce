package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.orderItems;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderItemMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderItem.IOrderItems.IOrderItemsResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orderItems")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "OrderItem Read", description = "Endpoints for reading order items")
public class ReadOrderItemsController extends BaseSoftDeleteReadController<OrderItem, OrderItemResponseDTO> {
    private final OrderItemMapper orderItemMapper;

    public ReadOrderItemsController(IOrderItemsResponseService orderItemsResponseService, ModelMapper modelMapper, OrderItemMapper orderItemMapper) {
        super(OrderItemResponseDTO.class,  orderItemsResponseService, modelMapper);
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    protected OrderItemResponseDTO convertToDto(OrderItem entity) {
        return this.orderItemMapper.toDTO(entity);
    }
}
