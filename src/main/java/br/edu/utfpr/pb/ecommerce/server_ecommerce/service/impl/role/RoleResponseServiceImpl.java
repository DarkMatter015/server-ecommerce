package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.RoleRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role.iRole.IRoleResponseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleResponseServiceImpl extends BaseResponseServiceImpl<Role, Long> implements IRoleResponseService {
    private final RoleRepository repository;

    public RoleResponseServiceImpl(RoleRepository repository,
                                   AuthService authService) {
        super(repository, authService);
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(Role.class, name));
    }
}
