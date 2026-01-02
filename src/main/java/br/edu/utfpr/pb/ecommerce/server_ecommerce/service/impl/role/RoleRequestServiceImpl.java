package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.role.RoleUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.RoleRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role.iRole.IRoleRequestService;
import org.springframework.stereotype.Service;

@Service
public class RoleRequestServiceImpl extends BaseRequestServiceImpl<Role, RoleUpdateDTO, Long> implements IRoleRequestService {
    public RoleRequestServiceImpl(RoleRepository repository,
                                  RoleResponseServiceImpl roleResponseService) {
        super(repository, roleResponseService);
    }
}
