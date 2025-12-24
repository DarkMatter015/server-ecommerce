package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public abstract class BaseSoftDeleteWriteController<T extends BaseSoftDeleteEntity, D, RD, UD> extends BaseWriteController<T, D, RD, UD, Long> {
    private final IBaseSoftDeleteRequestService<T, UD, Long> service;

    public BaseSoftDeleteWriteController(IBaseSoftDeleteRequestService<T, UD, Long> service,
                                         ModelMapper modelMapper,
                                         Class<T> typeClass,
                                         Class<RD> typeDtoResponseClass) {
        super(service, modelMapper, typeClass, typeDtoResponseClass);
        this.service = service;
    }

    @Operation(summary = "Activate entity", description = "Activates an entity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity activated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @PostMapping("activate/{id}")
    public ResponseEntity<RD> activate(@Parameter(description = "Entity ID") @PathVariable Long id) {
        T entity = this.service.activate(id);
        return ResponseEntity.ok(convertToResponseDto(entity));
    }
}
