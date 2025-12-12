package br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService.isAuthenticated;

public final class AuthValidation {
    private AuthValidation(){}

    public static boolean isAdmin(User user){
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }

    public static boolean isAuthenticatedAndAdmin(AuthService authService) {
        return isAuthenticated() && isAdmin(authService.getAuthenticatedUser());
    }
}
