package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.RoleNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.UserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password.IncorrectPasswordException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.RoleRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAdmin;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAuthenticatedAndAdmin;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateStringNullOrBlank;


@Service
public class UserRequestServiceImpl extends CrudRequestServiceImpl<User, UserUpdateDTO, Long> implements IUserRequestService {

    private final UserRepository userRepository;
    private final UserResponseServiceImpl userResponseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;

    public UserRequestServiceImpl(UserRepository userRepository, UserResponseServiceImpl userResponseService, AuthService authService, RoleRepository roleRepository, AddressRepository addressRepository) {
        super(userRepository, userResponseService);
        this.userRepository = userRepository;
        this.userResponseService = userResponseService;
        this.authService = authService;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    private void encodePassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    private User findAndValidateUser(Long id, User authenticatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (isAdmin(authenticatedUser))
            return existingUser;

        if (!existingUser.getId().equals(authenticatedUser.getId()))
            throw new AccessDeniedException("You don't have permission to modify this user.");

        return existingUser;
    }

    @Override
    @Transactional
    public User activate(Long id) {
        User user = userResponseService.findById(id);
        if (user.isActive()) return user;
        addressRepository.activateByUserId(user.getId());
        user.setDeletedAt(null);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setDisplayName(userRequestDTO.getDisplayName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
        Optional<User> optionalUser = userRepository.findByCpf(userRequestDTO.getCpf());
        if (optionalUser.isPresent()) throw new BusinessException("CPF already in use.");
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
        User existingUser = findAndValidateUser(id,  authenticatedUser);

        if (dto.getDisplayName() != null) {
            validateStringNullOrBlank(dto.getDisplayName());
            existingUser.setDisplayName(dto.getDisplayName());
        }

        if (dto.getEmail() != null) {
            validateStringNullOrBlank(dto.getEmail());
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getCpf() != null) {
            validateStringNullOrBlank(dto.getCpf());
            existingUser.setCpf(dto.getCpf());
        }

        if (!isAdmin(authenticatedUser) && dto.getRoles() != null) {
            throw new AccessDeniedException("You don't have permission to update this user roles.");
        } else if (isAdmin(authenticatedUser) && dto.getRoles() != null) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RoleNotFoundException("Role is not found: " + roleName)))
                    .collect(Collectors.toSet());
            existingUser.setRoles(roles);
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User authenticatedUser = authService.getAuthenticatedUser();
        findAndValidateUser(id, authenticatedUser);
        addressRepository.softDeleteByUserId(id);
        userRepository.softDeleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends User> iterable) {
        User authenticatedUser = authService.getAuthenticatedUser();
        iterable.forEach(user -> {
            findAndValidateUser(user.getId(), authenticatedUser);
            addressRepository.softDeleteByUserId(user.getId());
            userRepository.softDeleteById(user.getId());
        });
    }

    public void changePassword(ChangePasswordDTO dto) {
        if (!bCryptPasswordEncoder.matches(dto.getCurrentPassword(), authService.getAuthenticatedUser().getPassword())) throw new IncorrectPasswordException("The current password is incorrect.");
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) throw new IncorrectPasswordException("The confirm password does not match the new password");
        User user = authService.getAuthenticatedUser();
        user.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
