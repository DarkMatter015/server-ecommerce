package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.AuthenticatedUserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.UserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
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
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    public static boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getName().contains("anonymous");
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getName().equals("anonymousUser")) {
            throw new AuthenticatedUserNotFoundException("No authenticated user found");
        }

        String id = authentication.getName();
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        if (user.isEmpty()) {
            throw new AuthenticatedUserNotFoundException("Authenticated user not found!");
        }

        return user.get();
    }

    public User loadUserByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf);
        if (user == null) {
            throw new UserNotFoundException("User not found with cpf: " + cpf);
        }
        return user;
    }
}