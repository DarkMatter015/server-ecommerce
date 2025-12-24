package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;

import java.util.Optional;

public interface RoleRepository extends BaseSoftDeleteRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
