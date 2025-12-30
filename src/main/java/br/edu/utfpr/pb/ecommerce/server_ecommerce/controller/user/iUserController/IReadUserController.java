package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.user.iUserController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "User Read", description = "Endpoints for reading users")
public interface IReadUserController {
}
