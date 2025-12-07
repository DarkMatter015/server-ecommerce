package br.edu.utfpr.pb.ecommerce.server_ecommerce.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.UserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidStringException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import org.springframework.security.access.AccessDeniedException;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService.isAuthenticated;

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

    public static boolean isAdmin(User user){
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }

    public static boolean isAuthenticatedAndAdmin(AuthService authService) {
        return isAuthenticated() && isAdmin(authService.getAuthenticatedUser());
    }

    public static User findAndValidateUser(Long id, User authenticatedUser, UserRepository userRepository) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (isAdmin(authenticatedUser))
            return existingUser;

        if (!existingUser.getId().equals(authenticatedUser.getId()))
            throw new AccessDeniedException("You don't have permission to modify this user.");

        return existingUser;
    }
}
