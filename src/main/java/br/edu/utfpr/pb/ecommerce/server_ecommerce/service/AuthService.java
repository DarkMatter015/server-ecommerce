package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.AuthenticatedUserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    public boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getName().contains("anonymous");
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getName().equals("anonymousUser")) {
            throw new AuthenticatedUserNotFoundException("No authenticated user found");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        if ( user == null) throw new AuthenticatedUserNotFoundException("Authenticated user not found!");

        return user;
    }

    public User loadUserByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with cpf: " + cpf);
        }
        return user;
    }
}