package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Ownable;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.specification.BaseSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAuthenticatedAndAdmin;

public abstract class CrudResponseServiceImpl<T extends BaseEntity, ID extends Serializable> implements ICrudResponseService<T, ID> {

    private final BaseRepository<T, ID> repository;
    private final AuthService authService;
    private final Class<T> entityClass;

    public CrudResponseServiceImpl(BaseRepository<T, ID> repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private Specification<T> buildContextSpecification() {
        // REGRA 1: ADMIN vê tudo (sem filtro)
        if (isAuthenticatedAndAdmin(authService))
            return null;

        // REGRA 2: Dono vê seus registros (ativos e inativos)
        if (Ownable.class.isAssignableFrom(entityClass))
            return BaseSpecification.isOwnedBy(authService.getAuthenticatedUser().getId());

        // REGRA 3: Padrão (Público) vê apenas ativos
        return BaseSpecification.isNotDeleted();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        Specification<T> spec = buildContextSpecification();
        return repository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Sort sort) {
        Specification<T> spec = buildContextSpecification();

        if (sort == null)
            sort = Sort.unsorted();

        return repository.findAll(spec, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        Specification<T> spec = buildContextSpecification();

        if (pageable == null)
            pageable = Pageable.unpaged();

        return repository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public void flush() {
        this.repository.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(ID id) {
        Specification<T> contextSpec = buildContextSpecification();
        Specification<T> idSpec = (root, query, cb)
                -> cb.equal(root.get("id"), id);
        return this.repository.findOne(idSpec.and(contextSpec))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        Specification<T> contextSpec = buildContextSpecification();
        Specification<T> idSpec = (root, query, cb) -> cb.equal(root.get("id"), id);
        return this.repository.exists(idSpec.and(contextSpec));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        Specification<T> contextSpec = buildContextSpecification();
        return this.repository.count(contextSpec);
    }
}
