package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<PasswordResetToken> findByToken(String token);

    @EntityGraph(attributePaths = "user")
    Optional<PasswordResetToken> findByUser(User user);
}
