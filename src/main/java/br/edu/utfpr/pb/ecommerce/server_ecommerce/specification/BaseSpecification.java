package br.edu.utfpr.pb.ecommerce.server_ecommerce.specification;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {
    public static <T> Specification<T> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static <T> Specification<T> isOwnedBy(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }
}
