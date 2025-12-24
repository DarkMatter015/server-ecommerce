package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;

import java.util.Optional;

public interface UserRepository extends BaseSoftDeleteRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);
}
