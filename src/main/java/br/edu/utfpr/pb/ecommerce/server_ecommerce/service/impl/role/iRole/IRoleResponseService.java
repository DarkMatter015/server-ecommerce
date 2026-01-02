package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role.iRole;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseResponseService;

public interface IRoleResponseService extends IBaseResponseService<Role, Long> {
    Role findByName(String name);
}
