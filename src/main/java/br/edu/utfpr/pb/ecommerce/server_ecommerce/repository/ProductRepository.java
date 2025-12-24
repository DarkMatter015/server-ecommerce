package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseSoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends BaseSoftDeleteRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Modifying
    @Query("UPDATE Product p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.category.id = :categoryId")
    void softDeleteByCategoryId(@Param("categoryId") Long categoryId);

    @Modifying
    @Query("UPDATE Product p SET p.deletedAt = NULL WHERE p.category.id = :categoryId")
    void activateByCategoryId(@Param("categoryId") Long categoryId);
}
