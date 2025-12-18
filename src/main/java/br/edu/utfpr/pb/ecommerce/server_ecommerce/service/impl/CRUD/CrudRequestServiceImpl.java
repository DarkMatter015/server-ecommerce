package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.getNullPropertyNames;

@RequiredArgsConstructor
public abstract class CrudRequestServiceImpl<T extends BaseEntity, UD, ID extends Serializable> implements ICrudRequestService<T, UD, ID> {

    private final BaseRepository<T, ID> repository;
    private final CrudResponseServiceImpl<T, ID> crudResponseService;

    /**
     * Copia as propriedades não-nulas do DTO para a Entidade.
     * @param sourceDTO O objeto com os novos dados (pode ter campos null)
     * @param targetEntity A entidade do banco que será atualizada
     */
    protected void applyPartialUpdate(Object sourceDTO, T targetEntity) {
        BeanUtils.copyProperties(sourceDTO, targetEntity, getNullPropertyNames(sourceDTO));
    }

    @Override
    @Transactional
    public T activate(ID id) {
        T entity = crudResponseService.findById(id);
        if (entity.isActive()) return entity;
        entity.setDeletedAt(null);
        return repository.save(entity);
    }
    
    @Override
    @Transactional
    public T save(T entity) {
        return this.repository.save(entity);
    }

    @Override
    @Transactional
    public T saveAndFlush(T entity) {
        return this.repository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public Iterable<T> save(Iterable<T> iterable) {
        return this.repository.saveAll(iterable);
    }

    @Override
    @Transactional
    public T update(ID id, UD updateDTO){
        T entity = crudResponseService.findById(id);
        applyPartialUpdate(updateDTO, entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends T> iterable) {
        this.repository.deleteAll(iterable);
    }
}
