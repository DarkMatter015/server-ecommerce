package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.address.iAddressController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "Address Read", description = "Endpoints for reading addresses")
public interface IReadAddressController {
}
