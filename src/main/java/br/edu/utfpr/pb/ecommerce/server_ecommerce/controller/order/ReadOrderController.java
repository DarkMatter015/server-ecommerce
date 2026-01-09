package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order.iOrderController.IReadOrderController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderAIDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.IOrder.IOrderResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class ReadOrderController extends BaseSoftDeleteReadController<Order, OrderResponseDTO> implements IReadOrderController {

    private final OrderMapper orderMapper;
    private final IOrderResponseService orderResponseService;

    public ReadOrderController(IOrderResponseService orderResponseService, ModelMapper modelMapper, OrderMapper orderMapper) {
        super(OrderResponseDTO.class, orderResponseService, modelMapper);
        this.orderMapper = orderMapper;
        this.orderResponseService = orderResponseService;
    }

    @Override
    protected OrderResponseDTO convertToDto(Order entity) {
        return this.orderMapper.toDTO(entity);
    }

    @Override
    @GetMapping("/ai/{id}")
    public ResponseEntity<OrderAIDTO> getOrderForAI(@PathVariable Long id) {
        return ResponseEntity.ok(this.orderResponseService.getOrderForAI(id));
    }
}
