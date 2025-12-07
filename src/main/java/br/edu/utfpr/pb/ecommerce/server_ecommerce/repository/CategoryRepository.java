package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends BaseRepository<Category,Long>, JpaSpecificationExecutor<Category> {
}
