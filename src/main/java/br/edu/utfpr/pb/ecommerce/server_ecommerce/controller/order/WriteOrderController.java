package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class WriteOrderController {
    private final OrderRequestServiceImpl orderRequestService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderDTO, UriComponentsBuilder uriBuilder) {
        Order savedOrder = orderRequestService.createOrder(orderDTO);
        return ResponseEntity.created(createUri(savedOrder)).body(orderMapper.toDTO(savedOrder));
    }

    @PatchMapping("{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderUpdateDTO updateDTO) {
        Order order = orderRequestService.update(id, updateDTO);

        return ResponseEntity.ok(orderMapper.toDTO(order));
    }
}
