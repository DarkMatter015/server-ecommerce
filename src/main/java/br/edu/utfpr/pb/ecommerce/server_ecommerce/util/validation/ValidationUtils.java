package br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidStringException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;

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
}
