package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.AuthUserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundWithException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.SecurityUserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found with email: " + email);
        return user.get();
    }

    private static boolean validateAuthenticatedUser(Authentication authentication) {
        return !(authentication == null || !authentication.isAuthenticated() || authentication.getName().contains("anonymous"));
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return validateAuthenticatedUser(authentication);
    }

    public User getAuthenticatedUser() {
        if (!isAuthenticated()) throw new AuthUserNotFoundException(ErrorCode.AUTHENTICATED_USER_NOT_FOUND);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        if (user.isEmpty()) throw new AuthUserNotFoundException(ErrorCode.AUTHENTICATED_USER_NOT_FOUND);

        return user.get();
    }

    public User loadUserByCpf(String cpf) {
        Optional<User> user = userRepository.findByCpf(cpf);
        if (user.isEmpty()) throw new ResourceNotFoundWithException("cpf", cpf);
        return user.get();
    }

    public AuthenticationResponseDTO validateUserToken() {
        User user = getAuthenticatedUser();
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setUser(new SecurityUserResponseDTO(user));
        return response;
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!validateAuthenticatedUser(authentication))
            throw new AuthUserNotFoundException(ErrorCode.AUTHENTICATED_USER_NOT_FOUND);

        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            throw new AuthUserNotFoundException(ErrorCode.AUTHENTICATED_USER_NOT_FOUND);
        }
    }
}