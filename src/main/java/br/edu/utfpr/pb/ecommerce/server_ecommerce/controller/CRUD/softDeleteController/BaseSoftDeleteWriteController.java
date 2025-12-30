package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.base.BaseWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.iSoftDeleteController.IWriteSoftDeleteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public abstract class BaseSoftDeleteWriteController<T extends BaseSoftDeleteEntity, D, RD, UD> extends BaseWriteController<T, D, RD, UD, Long> implements IWriteSoftDeleteController<RD> {
    private final IBaseSoftDeleteRequestService<T, UD, Long> service;

    public BaseSoftDeleteWriteController(IBaseSoftDeleteRequestService<T, UD, Long> service,
                                         ModelMapper modelMapper,
                                         Class<T> typeClass,
                                         Class<RD> typeDtoResponseClass) {
        super(service, modelMapper, typeClass, typeDtoResponseClass);
        this.service = service;
    }

    @Override
    @PostMapping("activate/{id}")
    public ResponseEntity<RD> activate(@PathVariable Long id) {
        T entity = this.service.activate(id);
        return ResponseEntity.ok(convertToResponseDto(entity));
    }

    @Override
    @DeleteMapping("inactivate/{id}")
    public ResponseEntity<Void> softDeleteById(@PathVariable Long id) {
        this.service.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
