package br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidStringException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static void validateQuantityOfProducts(Integer quantity, Product product) {
        if (quantity == null || quantity > product.getQuantityAvailableInStock())
            throw new InvalidQuantityException("Quantity greater than that available in the product stock. Quantity available in stock: "
                    + product.getQuantityAvailableInStock());
    }

    public static void validateStringNullOrBlank(String fieldName) {
        if (fieldName == null || fieldName.isBlank())
            throw new InvalidStringException("String cannot be null or blank.");
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
}
