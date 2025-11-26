package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
}
