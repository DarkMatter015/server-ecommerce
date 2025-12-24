package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteResponseService;
import org.modelmapper.ModelMapper;

public abstract class BaseSoftDeleteReadController<T extends BaseSoftDeleteEntity, RD> extends BaseReadController<T, RD, Long> {

    public BaseSoftDeleteReadController(Class<RD> typeDtoClass, IBaseSoftDeleteResponseService<T, Long> service, ModelMapper modelMapper) {
        super(typeDtoClass, service, modelMapper);
    }
}
