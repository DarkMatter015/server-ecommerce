package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@RequiredArgsConstructor
public abstract class CrudRequestServiceImpl<T extends BaseEntity, UD, ID extends Serializable> implements ICrudRequestService<T, UD, ID> {

    private final BaseRepository<T, ID> repository;
    
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
    public abstract T update(ID id, UD updateDTO);

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

    @Override
    @Transactional
    public void deleteAll() {
        this.repository.deleteAll();
    }
}
