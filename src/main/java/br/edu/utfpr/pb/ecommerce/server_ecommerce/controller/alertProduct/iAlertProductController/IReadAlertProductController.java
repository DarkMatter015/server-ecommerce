package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct.iAlertProductController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@SecurityRequirement(name = "bearer-key")
@Tag(name = "Alerts Read", description = "Endpoints for reading alerts of available products")
public interface IReadAlertProductController {
}
