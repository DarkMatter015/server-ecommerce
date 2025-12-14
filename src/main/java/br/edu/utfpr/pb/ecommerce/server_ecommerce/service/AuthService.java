package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.AuthenticatedUserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.UserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.IncorrectPasswordException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.AuthenticationResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.ChangePasswordRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.SecurityUserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found with email: " + email);
        return user;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication == null || !authentication.isAuthenticated() || authentication.getName().contains("anonymous"));
    }

    public User getAuthenticatedUser() {
        if (!isAuthenticated()) throw new AuthenticatedUserNotFoundException("No authenticated user found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        if (user.isEmpty()) throw new AuthenticatedUserNotFoundException("Authenticated user not found!");

        return user.get();
    }

    public User loadUserByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf);
        if (user == null) throw new UserNotFoundException("User not found with cpf: " + cpf);
        return user;
    }

    public AuthenticationResponseDTO validateUserToken() {
        User user = getAuthenticatedUser();
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setUser(new SecurityUserResponseDTO(user));
        return response;
    }

    public User changePassword(ChangePasswordRequestDTO dto) {
        if (!passwordEncoder.matches(dto.getCurrent_password(), getAuthenticatedUser().getPassword())) throw new IncorrectPasswordException("The current password is incorrect.");
        if (!dto.getNew_password().equals(dto.getConfirm_password())) throw new IncorrectPasswordException("The confirm password does not match the new password");
        User user = getAuthenticatedUser();
        user.setPassword(passwordEncoder.encode(dto.getNew_password()));
        return userRepository.save(user);
    }
}