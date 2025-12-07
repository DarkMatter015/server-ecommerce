package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;

import java.util.Optional;

public interface RoleRepository extends BaseRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
