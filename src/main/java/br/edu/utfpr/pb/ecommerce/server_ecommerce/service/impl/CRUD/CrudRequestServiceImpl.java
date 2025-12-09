package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.ICRUD.ICrudRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public abstract class CrudRequestServiceImpl<T extends BaseEntity, UD, ID extends Serializable> implements ICrudRequestService<T, UD, ID> {

    private final BaseRepository<T, ID> repository;
    private final CrudResponseServiceImpl<T, ID> crudResponseService;

    @Transactional
    public void activate(ID id) {
        T entity = crudResponseService.findById(id);

        entity.setDeletedAt(null);

        repository.save(entity);
    }

    /**
     * Copia as propriedades não-nulas do DTO para a Entidade.
     * @param sourceDTO O objeto com os novos dados (pode ter campos null)
     * @param targetEntity A entidade do banco que será atualizada
     */
    protected void applyPartialUpdate(Object sourceDTO, T targetEntity) {
        BeanUtils.copyProperties(sourceDTO, targetEntity, getNullPropertyNames(sourceDTO));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
            if (srcValue instanceof String && ((String) srcValue).isBlank()) emptyNames.add(pd.getName());
        }

        // Campos que não devem ser sobrescritos (redundancia)
        emptyNames.add("id");

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
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
    };

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
