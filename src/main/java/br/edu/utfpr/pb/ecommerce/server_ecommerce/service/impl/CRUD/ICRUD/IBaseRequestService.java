package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD;

import java.io.Serializable;

public interface IBaseRequestService<T, UD, ID extends Serializable>{
    T save(T entity);

    T saveAndFlush(T entity);

    Iterable<T> save(Iterable<T> iterable);

    T update(ID id, UD updateDTO);

    void deleteById(ID id);

    void delete(Iterable<? extends T> iterable);
}
