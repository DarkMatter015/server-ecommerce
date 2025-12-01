package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.RoleNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.RoleRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.*;


@Service
public class UserRequestServiceImpl extends CrudRequestServiceImpl<User, UserUpdateDTO, Long> implements IUserRequestService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;
    private final RoleRepository roleRepository;

    public UserRequestServiceImpl(UserRepository userRepository, AuthService authService, RoleRepository roleRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.authService = authService;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    private void encodePassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    @Override
    @Transactional
    public User createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setDisplayName(userRequestDTO.getDisplayName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
        user.setCpf(userRequestDTO.getCpf());

        if (!isAuthenticatedAndAdmin(authService)) {
            if (userRequestDTO.getRoles() != null && !userRequestDTO.getRoles().equals(Collections.singleton("USER"))) {
                throw new AccessDeniedException("You don't have permission to create this user with roles.");
            }
            Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
            user.setRoles(Collections.singleton(userRole));
        } else {
            if (userRequestDTO.getRoles() == null) {
                Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                user.setRoles(Collections.singleton(userRole));
            } else {
                Set<Role> roles = userRequestDTO.getRoles().stream()
                        .map(roleName -> roleRepository.findByName(roleName)
                                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found.")))
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            }
        }

        return super.save(user);
    }

    @Override
    @Transactional
    public User save(User entity) {
        encodePassword(entity);
        return super.save(entity);
    }

    @Override
    @Transactional
    public User saveAndFlush(User entity) {
        encodePassword(entity);
        return super.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public Iterable<User> save(Iterable<User> iterable) {
        iterable.forEach(this::encodePassword);
        return super.save(iterable);
    }

    @Override
    @Transactional
    public User update(Long id, UserUpdateDTO dto) {
        User authenticatedUser = authService.getAuthenticatedUser();
        User existingUser = findAndValidateUser(id,  authenticatedUser, userRepository);

        if (dto.getDisplayName() != null) {
            validateStringNullOrBlank(dto.getDisplayName());
            existingUser.setDisplayName(dto.getDisplayName());
        }

        if (dto.getEmail() != null) {
            validateStringNullOrBlank(dto.getEmail());
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            validateStringNullOrBlank(dto.getPassword());
            existingUser.setPassword(dto.getPassword());
            encodePassword(existingUser);
        }

        if (dto.getCpf() != null) {
            validateStringNullOrBlank(dto.getCpf());
            existingUser.setCpf(dto.getCpf());
        }

        if (!isAdmin(authenticatedUser)) {
            if (dto.getRoles() != null) {
                throw new AccessDeniedException("You don't have permission to update this user roles.");
            }
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User authenticatedUser = authService.getAuthenticatedUser();
        User existingUser = findAndValidateUser(id, authenticatedUser, userRepository);
        userRepository.delete(existingUser);
    }

    @Override
    @Transactional
    public void deleteAll() {
        super.deleteAll();
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends User> iterable) {
        super.delete(iterable);
    }
}
