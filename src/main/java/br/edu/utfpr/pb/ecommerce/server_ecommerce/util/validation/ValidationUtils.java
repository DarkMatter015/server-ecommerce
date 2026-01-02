package br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static void validateQuantityOfProduct(Integer quantity, Product product) {
        if (quantity == null || quantity > product.getQuantityAvailableInStock())
            throw new InvalidQuantityException(ErrorCode.PRODUCT_QUANTITY_INVALID, product.getId(), product.getName(), product.getQuantityAvailableInStock());
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
            if (srcValue instanceof String && ((String) srcValue).trim().isBlank()) emptyNames.add(pd.getName());
        }

        // Campos que não devem ser sobrescritos (redundancia)
        emptyNames.add("id");

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static Order findAndValidateOrder(Long id, User user, OrderRepository orderRepository) {
        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, id));
    }
}
