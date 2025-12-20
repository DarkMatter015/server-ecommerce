package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.orderItems;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.WriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderItem.OrderItemsRequestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

@RestController
@RequestMapping("orderItems")
@Tag(name = "OrderItem Write", description = "Endpoints for writing order items")
public class WriteOrderItemsController extends WriteController<OrderItem, OrderItemRequestDTO, OrderItemResponseDTO, OrderItemUpdateDTO, Long> {
    private final OrderItemsRequestServiceImpl orderItemsRequestService;

    public WriteOrderItemsController(OrderItemsRequestServiceImpl orderItemsRequestService, ModelMapper modelMapper) {
        super(orderItemsRequestService, modelMapper, OrderItem.class, OrderItemResponseDTO.class);
        this.orderItemsRequestService = orderItemsRequestService;
    }

    @Operation(summary = "Create order item", description = "Creates a new order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @Override
    public ResponseEntity<OrderItemResponseDTO> create(@RequestBody @Valid OrderItemRequestDTO dto) {
        OrderItem savedItem = orderItemsRequestService.createOrderItem(dto);
        return ResponseEntity.created(createUri(savedItem)).body(convertToResponseDto(savedItem));
    }

}
