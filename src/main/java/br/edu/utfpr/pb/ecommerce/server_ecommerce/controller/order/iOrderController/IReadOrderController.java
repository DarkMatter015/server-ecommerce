package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order.iOrderController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order Read", description = "Endpoints for reading orders")
public interface IReadOrderController {
}
