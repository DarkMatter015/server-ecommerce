package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Ownable;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.specification.BaseSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAuthenticatedAndAdmin;

public abstract class BaseResponseServiceImpl<T, ID extends Serializable> implements IBaseResponseService<T, ID> {

    private final BaseRepository<T, ID> repository;
    private final AuthService authService;
    private final Class<T> entityClass;

    public BaseResponseServiceImpl(BaseRepository<T, ID> repository,
                                   AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Specification<T> buildContextSpecification() {
        // REGRA 1: ADMIN vê tudo (sem filtro)
        if (isAuthenticatedAndAdmin())
            return null;

        // REGRA 2: Dono vê seus registros (ativos e inativos)
        if (Ownable.class.isAssignableFrom(entityClass))
            return BaseSpecification.isOwnedBy(authService.getAuthenticatedUserId());

        // REGRA 3: Hook para comportamento específico (Abstrato)
        return getDefaultVisibilitySpecification();
    }

    /**
     * Método gancho (Hook) que as subclasses PODEM implementar.
     * Aqui define-se o que acontece se não for Admin nem Dono.
     */
    protected Specification<T> getDefaultVisibilitySpecification(){
        return null;
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
    @Transactional(readOnly = true)
    public List<T> findAll(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }

        Specification<T> idsSpec = (root, query, cb) -> {
            CriteriaBuilder.In<Object> inClause = cb.in(root.get("id"));
            ids.forEach(inClause::value);
            return inClause;
        };

        Specification<T> contextSpec = buildContextSpecification();

        Specification<T> finalSpec = (contextSpec == null)
                ? idsSpec
                : idsSpec.and(contextSpec);

        return repository.findAll(finalSpec);
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
        Specification<T> idSpec = (root, query, cb) -> cb.equal(root.get("id"), id);

        Specification<T> finalSpec = (contextSpec == null) ? idSpec : idSpec.and(contextSpec);

        return this.repository.findOne(finalSpec)
                .orElseThrow(() -> new ResourceNotFoundException(entityClass, id));
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
