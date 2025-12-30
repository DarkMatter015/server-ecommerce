package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.orderItems;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.orderItems.iOrderItemsController.IWriteOrderItemsController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderItem.OrderItemsRequestServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

@RestController
@RequestMapping("orderItems")
public class WriteOrderItemsController extends BaseSoftDeleteWriteController<OrderItem, OrderItemRequestDTO, OrderItemResponseDTO, OrderItemUpdateDTO> implements IWriteOrderItemsController {
    private final OrderItemsRequestServiceImpl orderItemsRequestService;

    public WriteOrderItemsController(OrderItemsRequestServiceImpl orderItemsRequestService, ModelMapper modelMapper) {
        super(orderItemsRequestService, modelMapper, OrderItem.class, OrderItemResponseDTO.class);
        this.orderItemsRequestService = orderItemsRequestService;
    }

    @Override
    public ResponseEntity<OrderItemResponseDTO> create(@RequestBody @Valid OrderItemRequestDTO dto) {
        OrderItem savedItem = orderItemsRequestService.createOrderItem(dto);
        return ResponseEntity.created(createUri(savedItem)).body(convertToResponseDto(savedItem));
    }

}
