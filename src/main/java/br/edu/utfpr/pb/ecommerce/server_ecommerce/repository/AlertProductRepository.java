package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertProductRepository extends BaseRepository<AlertProduct,Long> {
    List<AlertProduct> findAllByProduct_IdAndStatus(Long productId, AlertProductStatus status);
    boolean existsByEmailAndProductAndStatus(String email, Product product, AlertProductStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AlertProduct a SET a.user = :user WHERE a.email = :email AND a.user IS NULL")
    void linkOrphanAlertsToUser(@Param("email") String email, @Param("user") User user);
}
