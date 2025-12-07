package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.FilterManager;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudResponseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public abstract class CrudResponseServiceImpl<T extends BaseEntity, ID extends Serializable> implements ICrudResponseService<T, ID> {

    private final BaseRepository<T, ID> repository;
    private final FilterManager filterManager;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    @Transactional
    public void flush() {
        this.repository.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(ID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return this.repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return this.repository.count();
    }
}
