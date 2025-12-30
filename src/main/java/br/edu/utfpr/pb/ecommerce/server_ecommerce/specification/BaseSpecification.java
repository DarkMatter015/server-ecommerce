package br.edu.utfpr.pb.ecommerce.server_ecommerce.specification;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BaseSpecification {
    private static final Map<Class<?>, Function<Root<?>, Path<?>>> USER_PATH_STRATEGIES = new HashMap<>();

    static {
        // Para OrderItem, navegue: OrderItem -> Order -> User
        USER_PATH_STRATEGIES.put(OrderItem.class, root -> root.get("order").get("user"));
    }

    public static <T> Specification<T> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static <T> Specification<T> isOwnedBy(Long userId) {
        return (root, query, cb) -> {
            Class<?> entityClass = root.getJavaType();

            Function<Root<?>, Path<?>> pathStrategy = USER_PATH_STRATEGIES.getOrDefault(
                    entityClass,
                    r -> r.get("user") // Fallback padrão
            );

            Path<?> userPath = pathStrategy.apply(root);

            return cb.equal(userPath.get("id"), userId);
        };
    }
}
