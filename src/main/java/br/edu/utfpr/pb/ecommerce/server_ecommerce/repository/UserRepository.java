package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;

public interface UserRepository extends BaseRepository<User, Long> {
    User findByEmail(String email);

    User findByCpf(String cpf);
}
