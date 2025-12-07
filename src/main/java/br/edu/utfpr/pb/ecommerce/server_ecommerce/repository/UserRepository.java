package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;

public interface UserRepository extends BaseRepository<User, Long> {
    User findByEmail(String email);

    User findByCpf(String cpf);
}
