package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD;

import java.io.Serializable;

public interface IBaseSoftDeleteRequestService<T, UD, ID extends Serializable> extends IBaseRequestService<T, UD, ID> {

    T activate(ID id);
}
