package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order.iOrderController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderAIDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order Read", description = "Endpoints for reading orders")
public interface IReadOrderController {
    ResponseEntity<OrderAIDTO> getOrderForAI(@PathVariable Long id);
}
