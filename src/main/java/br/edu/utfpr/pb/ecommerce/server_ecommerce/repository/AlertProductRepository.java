package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
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

public interface AlertProductRepository extends BaseRepository<AlertProduct,Long> {
    @EntityGraph(attributePaths = {"user", "user.roles", "product"})
    List<AlertProduct> findAllByProduct_IdAndStatus(Long productId, AlertProductStatus status);
    @EntityGraph(attributePaths = {"product"})
    boolean existsByEmailAndProductAndStatus(String email, Product product, AlertProductStatus status);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles", "product"})
    Optional<AlertProduct> findById(@Nullable Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles", "product"})
    Optional<AlertProduct> findOne(@Nullable Specification<AlertProduct> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles", "product"})
    List<AlertProduct> findAll(@Nullable Specification<AlertProduct> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "user.roles", "product"})
    Page<AlertProduct> findAll(@Nullable Specification<AlertProduct> spec,@Nullable Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AlertProduct a SET a.user = :user WHERE a.email = :email AND a.user IS NULL")
    void linkOrphanAlertsToUser(@Param("email") String email, @Param("user") User user);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AlertProduct a SET a.status = :status WHERE a.id = :id")
    void updateAlertStatus(@Param("id") Long id, @Param("status") AlertProductStatus status);
}
