package br.edu.utfpr.pb.ecommerce.server_ecommerce.specification;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ProductSpecification {

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategoryNameLike(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null) {
                return null;
            }
            Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(categoryJoin.get("name")),
                    "%" + categoryName.toLowerCase() + "%"
            );
        };
    }
}
