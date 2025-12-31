package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends BaseSoftDeleteRepository<Address,Long> {
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<Address> findByIdAndUser(Long id, User user);

    @Modifying
    @Query("UPDATE Address a SET a.deletedAt = CURRENT_TIMESTAMP WHERE a.user.id = :userId")
    void softDeleteByUserId(@Param("userId") Long userId);

    void deleteAllByUser_Id(Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.deletedAt = NULL WHERE a.user.id = :userId")
    void activateByUserId(@Param("userId") Long userId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles"})
    List<Address> findAll(Specification<Address> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Page<Address> findAll(Specification<Address> spec,@Nullable Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<Address> findById(@Nullable Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<Address> findOne(@Nullable Specification<Address> spec);
}
