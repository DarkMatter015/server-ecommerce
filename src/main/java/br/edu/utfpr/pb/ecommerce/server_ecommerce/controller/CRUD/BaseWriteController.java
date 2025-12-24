package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseIdEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.net.URI;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

// T = class type (User, Category...), D = DTO type (Request), RD = DTO type (Response), UD = DTO type (Update), ID = primary key attribute of the class
@SecurityRequirement(name = "bearer-key")
public abstract class BaseWriteController<T extends BaseIdEntity, D, RD, UD, ID extends Serializable> {

    private final IBaseRequestService<T, UD, ID> service;
    protected final ModelMapper modelMapper; // 'protected' to be used by 'hooks'

    private final Class<T> typeClass;
    private final Class<RD> typeDtoResponseClass;

    public BaseWriteController(IBaseRequestService<T, UD, ID> service,
                               ModelMapper modelMapper,
                               Class<T> typeClass,
                               Class<RD> typeDtoResponseClass) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.typeClass = typeClass;
        this.typeDtoResponseClass = typeDtoResponseClass;
    }

    // ResponseDTO ==> Entity
    protected T convertToEntity(D createDto) {
        return modelMapper.map(createDto, this.typeClass);
    }

    // Entity ==> ResponseDTO
    protected RD convertToResponseDto(T entity) {
        return modelMapper.map(entity, this.typeDtoResponseClass);
    }

    @Operation(summary = "Create entity", description = "Creates a new entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<RD> create(@RequestBody @Valid D entityDto) {
        T entity = convertToEntity(entityDto);
        T savedEntity = service.save(entity);

        URI uri = createUri(savedEntity);

        RD responseDto = convertToResponseDto(savedEntity);
        return ResponseEntity.created(uri).body(responseDto);
    }

    @Operation(summary = "Update entity", description = "Updates an existing entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PatchMapping("{id}")
    public ResponseEntity<RD> update(@Parameter(description = "Entity ID") @PathVariable ID id, @RequestBody @Valid UD entityDto) {

        T updatedEntity = this.service.update(id, entityDto);

        RD responseDto = convertToResponseDto(updatedEntity);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Delete entity", description = "Removes an entity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entity removed successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Entity ID") @PathVariable ID id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
