package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends BaseSoftDeleteRepository<Address,Long> {
    Optional<Address> findByIdAndUser(Long id, User user);

    @Modifying
    @Query("UPDATE Address a SET a.deletedAt = CURRENT_TIMESTAMP WHERE a.user.id = :userId")
    void softDeleteByUserId(@Param("userId") Long userId);

    void deleteAllByUser_Id(Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.deletedAt = NULL WHERE a.user.id = :userId")
    void activateByUserId(@Param("userId") Long userId);
}
