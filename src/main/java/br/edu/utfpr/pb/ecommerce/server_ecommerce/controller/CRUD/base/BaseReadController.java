package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.base;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.base.iBaseController.IBaseReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseIdEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseResponseService;
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
public abstract class BaseReadController<T extends BaseIdEntity, RD, ID extends Serializable> implements IBaseReadController<RD, ID> {

    private final IBaseResponseService<T, ID> service;
    protected final ModelMapper modelMapper; // 'protected' to be used by 'hooks'
    private final Class<RD> typeDtoClass;

    public BaseReadController(Class<RD> typeDtoClass,
                              IBaseResponseService<T, ID> service,
                              ModelMapper modelMapper) {
        this.typeDtoClass = typeDtoClass;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    protected RD convertToDto(T entity) {
        return this.modelMapper.map(entity, this.typeDtoClass);
    }

    @Override
    @GetMapping //http://ip-api:port/request-mapping
    public ResponseEntity<List<RD>> findAll() {
        return ResponseEntity.ok(this.service.findAll().stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Override
    @GetMapping("page") //http://ip-api:port/request-mapping/page?page=1&size=5
    public ResponseEntity<Page<RD>> findAll(@RequestParam int page,
                                           @RequestParam int size,
                                           @RequestParam(required = false) String order,
                                           @RequestParam(required = false) Boolean asc) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (order != null && asc != null) {
            pageRequest = PageRequest.of(page, size, asc ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        }
        return ResponseEntity.ok(this.service.findAll(pageRequest).map(this::convertToDto));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<RD> findOne(@PathVariable ID id) {
        T entity = this.service.findById(id);
        if (entity != null) {
            return ResponseEntity.ok(convertToDto(entity));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    @GetMapping("exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable ID id) {
        return ResponseEntity.ok(this.service.exists(id));
    }

    @Override
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(this.service.count());
    }

}
