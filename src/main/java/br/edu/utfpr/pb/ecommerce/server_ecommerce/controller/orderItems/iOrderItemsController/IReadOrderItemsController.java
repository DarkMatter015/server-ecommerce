package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.orderItems.iOrderItemsController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "OrderItem Read", description = "Endpoints for reading order items")
public interface IReadOrderItemsController {
}
