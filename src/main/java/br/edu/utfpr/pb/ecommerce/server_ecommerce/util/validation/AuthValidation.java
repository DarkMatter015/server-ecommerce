package br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService.isAuthenticated;

public final class AuthValidation {
    private AuthValidation(){}

    public static boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }

    public static boolean isAuthenticatedAndAdmin() {
        return isAuthenticated() && isAdmin();
    }
}
