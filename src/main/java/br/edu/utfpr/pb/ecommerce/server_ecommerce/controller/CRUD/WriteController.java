package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudRequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;

// T = class type (User, Category...), D = DTO type (Request), RD = DTO type (Response), UD = DTO type (Update), ID = primary key attribute of the class
@SecurityRequirement(name = "bearer-key")
public abstract class WriteController<T extends BaseEntity, D, RD, UD, ID extends Serializable> {

    private final ICrudRequestService<T, UD, ID> service;
    protected final ModelMapper modelMapper; // 'protected' to be used by 'hooks'

    private final Class<T> typeClass;
    private final Class<RD> typeDtoResponseClass;

    public WriteController(ICrudRequestService<T, UD, ID> service,
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

    @PostMapping
    public ResponseEntity<RD> create(@RequestBody @Valid D entityDto, UriComponentsBuilder uriBuilder) {
        T entity = convertToEntity(entityDto);
        T savedEntity = service.save(entity);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEntity.getId())
                .toUri();

        RD responseDto = convertToResponseDto(savedEntity);
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<RD> update(@PathVariable ID id, @RequestBody @Valid UD entityDto) {

        T updatedEntity = this.service.update(id, entityDto);

        RD responseDto = convertToResponseDto(updatedEntity);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("activate/{id}")
    public ResponseEntity<RD> activate(@PathVariable ID id) {
        T entity = this.service.activate(id);
        return ResponseEntity.ok(convertToResponseDto(entity));
    }
}
