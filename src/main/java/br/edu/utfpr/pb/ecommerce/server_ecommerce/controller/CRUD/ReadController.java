package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

// T = class type (User, Category...), RD = DTO type (Response), ID = primary key attribute of the class
public abstract class ReadController<T extends BaseEntity, RD, ID extends Serializable> {

    private final ICrudResponseService<T, ID> service;
    protected final ModelMapper modelMapper; // 'protected' to be used by 'hooks'
    private final Class<RD> typeDtoClass;

    public ReadController(Class<RD> typeDtoClass,
                          ICrudResponseService<T, ID> service,
                          ModelMapper modelMapper) {
        this.typeDtoClass = typeDtoClass;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    protected RD convertToDto(T entity) {
        return this.modelMapper.map(entity, this.typeDtoClass);
    }

    @Operation(summary = "List all", description = "Returns a list of all entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully listed entities")
    })
    @GetMapping //http://ip-api:port/request-mapping
    public ResponseEntity<List<RD>> findAll() {
        return ResponseEntity.ok(this.service.findAll().stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Operation(summary = "List all paginated", description = "Returns a paginated list of entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully listed entities")
    })
    @GetMapping("page") //http://ip-api:port/request-mapping/page?page=1&size=5
    public ResponseEntity<Page<RD>> findAll(@Parameter(description = "Page number (0..N)") @RequestParam int page,
                                           @Parameter(description = "Page size") @RequestParam int size,
                                           @Parameter(description = "Sort field") @RequestParam(required = false) String order,
                                           @Parameter(description = "Sort ascending (true) or descending (false)") @RequestParam(required = false) Boolean asc) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (order != null && asc != null) {
            pageRequest = PageRequest.of(page, size, asc ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        }
        return ResponseEntity.ok(this.service.findAll(pageRequest).map(this::convertToDto));
    }

    @Operation(summary = "Get entity by ID", description = "Returns a specific entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found entity"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<RD> findOne(@Parameter(description = "Entity ID") @PathVariable ID id) {
        T entity = this.service.findById(id);
        if (entity != null) {
            return ResponseEntity.ok(convertToDto(entity));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Check existence", description = "Checks if an entity exists by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification successful")
    })
    @GetMapping("exists/{id}")
    public ResponseEntity<Boolean> exists(@Parameter(description = "Entity ID") @PathVariable ID id) {
        return ResponseEntity.ok(this.service.exists(id));
    }

    @Operation(summary = "Count entities", description = "Returns the total number of entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count successful")
    })
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(this.service.count());
    }

}
