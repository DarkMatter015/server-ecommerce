package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Identifiable;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.getNullPropertyNames;

@RequiredArgsConstructor
public abstract class BaseRequestServiceImpl<T extends Identifiable<ID>, UD, ID extends Serializable> implements IBaseRequestService<T, UD, ID> {
    private final BaseRepository<T, ID> repository;
    private final BaseResponseServiceImpl<T, ID> crudResponseService;

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
        // 1. Separa o que é NOVO (Insert) do que é ATUALIZAÇÃO (Update)
        List<T> toInsert = new ArrayList<>();
        List<T> toUpdate = new ArrayList<>();
        List<ID> idsToCheck = new ArrayList<>();

        for (T entity : iterable) {
            if (entity.getId() == null) {
                toInsert.add(entity); // Novos sempre passam (criação)
            } else {
                toUpdate.add(entity);
                idsToCheck.add(entity.getId());
            }
        }

        // 2. Se tiver atualizações, valida permissão
        if (!toUpdate.isEmpty()) {
            // Busca quais IDs desses existem e pertencem ao usuário
            List<T> permittedInDb = crudResponseService.findAll(idsToCheck);

            // Cria um Set de IDs permitidos para busca rápida (O(1))
            Set<ID> permittedIds = permittedInDb.stream()
                    .map(Identifiable::getId)
                    .collect(Collectors.toSet());

            // 3. Filtra a lista de entrada (toUpdate) mantendo os DADOS NOVOS,
            // mas apenas dos IDs que foram permitidos.
            toUpdate = toUpdate.stream()
                    .filter(e -> permittedIds.contains(e.getId()))
                    .collect(Collectors.toList());

            // Se o tamanho diminuiu, significa que o usuário tentou
            // atualizar algo que não é dele.
            if (toUpdate.size() < idsToCheck.size()) {
                 throw new SecurityException("You dont have permission to update all this entities");
            }
        }

        List<T> finalToSave = new ArrayList<>(toInsert);
        finalToSave.addAll(toUpdate);

        if (finalToSave.isEmpty()) {
            return Collections.emptyList();
        }

        return this.repository.saveAll(finalToSave);
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
        crudResponseService.findById(id);
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends T> iterable) {
        List<ID> idsToCheck = StreamSupport.stream(iterable.spliterator(), false)
                .map(Identifiable::getId)
                .collect(Collectors.toList());

        List<T> safeToDelete = crudResponseService.findAll(idsToCheck);

        if (!safeToDelete.isEmpty()) {
            this.repository.deleteAll(safeToDelete);
        }
    }
}
