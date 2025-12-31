package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends BaseSoftDeleteRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Modifying
    @Query("UPDATE Product p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.category.id = :categoryId")
    void softDeleteByCategoryId(@Param("categoryId") Long categoryId);

    void deleteAllByCategory_Id(Long categoryId);

    @Modifying
    @Query("UPDATE Product p SET p.deletedAt = NULL WHERE p.category.id = :categoryId")
    void activateByCategoryId(@Param("categoryId") Long categoryId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "category")
    Page<Product> findAll(Specification<Product> spec,@Nullable Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "category")
    List<Product> findAll(@Nullable Specification<Product> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(@Nullable Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "category")
    Optional<Product> findOne(@Nullable Specification<Product> spec);
}
