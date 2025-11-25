package br.edu.utfpr.pb.ecommerce.server_ecommerce.specification;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CategorySpecification {

    public static Specification<Category> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }
}
