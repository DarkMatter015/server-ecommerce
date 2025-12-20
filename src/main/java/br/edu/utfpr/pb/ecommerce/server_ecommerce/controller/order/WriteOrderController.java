package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order Write", description = "Endpoints for writing orders")
public class WriteOrderController {
    private final OrderRequestServiceImpl orderRequestService;
    private final OrderMapper orderMapper;

    @Operation(summary = "Create order", description = "Creates a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderDTO) {
        Order savedOrder = orderRequestService.createOrder(orderDTO);
        return ResponseEntity.created(createUri(savedOrder)).body(orderMapper.toDTO(savedOrder));
    }

    @Operation(summary = "Update order", description = "Updates an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PatchMapping("{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@Parameter(description = "Order ID") @PathVariable Long id, @RequestBody @Valid OrderUpdateDTO updateDTO) {
        Order order = orderRequestService.update(id, updateDTO);
        return ResponseEntity.ok(orderMapper.toDTO(order));
    }
}
