package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role.iRole;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.role.RoleUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseRequestService;

public interface IRoleRequestService extends IBaseRequestService<Role, RoleUpdateDTO, Long> {
}
