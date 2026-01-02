package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseSoftDeleteRequestServiceImpl<T extends BaseSoftDeleteEntity, UD>
        extends BaseRequestServiceImpl<T, UD, Long> implements IBaseSoftDeleteRequestService<T, UD, Long> {

    private final BaseSoftDeleteRepository<T, Long> repository;
    private final BaseSoftDeleteResponseServiceImpl<T, Long> crudResponseService;

    public BaseSoftDeleteRequestServiceImpl(BaseSoftDeleteRepository<T, Long> baseSoftDeleteRepository,
                                            BaseSoftDeleteResponseServiceImpl<T, Long> softDeleteResponseService) {
        super(baseSoftDeleteRepository, softDeleteResponseService);
        this.repository = baseSoftDeleteRepository;
        this.crudResponseService = softDeleteResponseService;
    }

    @Override
    @Transactional
    public T activate(Long id) {
        T entity = crudResponseService.findById(id);
        if (entity.isActive()) return entity;
        entity.setDeletedAt(null);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        T entity = crudResponseService.findById(id);
        if (!entity.isActive()) return;
        repository.softDeleteById(id);
    }
}
