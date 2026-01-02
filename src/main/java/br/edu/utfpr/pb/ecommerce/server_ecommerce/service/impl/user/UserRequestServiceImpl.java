package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password.IncorrectPasswordException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Role;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.role.RoleResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.IUser.IUserRequestService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAdmin;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAuthenticatedAndAdmin;


@Service
public class UserRequestServiceImpl extends BaseSoftDeleteRequestServiceImpl<User, UserUpdateDTO> implements IUserRequestService {

    private final UserRepository userRepository;
    private final UserResponseServiceImpl userResponseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;
    private final RoleResponseServiceImpl roleResponseService;
    private final AddressRepository addressRepository;

    public UserRequestServiceImpl(UserRepository userRepository,
                                  UserResponseServiceImpl userResponseService,
                                  AuthService authService,
                                  RoleResponseServiceImpl roleResponseService,
                                  AddressRepository addressRepository) {
        super(userRepository, userResponseService);
        this.userRepository = userRepository;
        this.userResponseService = userResponseService;
        this.authService = authService;
        this.roleResponseService = roleResponseService;
        this.addressRepository = addressRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    private void encodePassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    private User findAndValidateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));

        if (isAdmin())
            return existingUser;

        if (!existingUser.getId().equals(authService.getAuthenticatedUserId()))
            throw new BusinessException(ErrorCode.USER_PERMISSION_MODIFY_DENIED);

        return existingUser;
    }

    private Set<Role> validateUpdateRoleUser(UserUpdateDTO dto) {
        if (dto.getRoles() != null && dto.getRoles().isEmpty())
            throw new BusinessException(ErrorCode.USER_ROLE_REQUIRED);
        if (!isAdmin() && dto.getRoles() != null) {
            throw new BusinessException(ErrorCode.USER_PERMISSION_ROLES_UPDATE_DENIED);
        } else if (isAdmin() && dto.getRoles() != null) {
            return dto.getRoles().stream()
                    .map(roleResponseService::findByName)
                    .collect(Collectors.toSet());
        }
        return null;
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
        if (optionalUser.isPresent()) throw new BusinessException(ErrorCode.USER_CPF_IN_USE);
        user.setCpf(userRequestDTO.getCpf());

        if (!isAuthenticatedAndAdmin()) {
            if (userRequestDTO.getRoles() != null && !userRequestDTO.getRoles().equals(Collections.singleton("USER"))) {
                throw new BusinessException(ErrorCode.USER_PERMISSION_CREATE_ROLES_DENIED);
            }
            Role userRole = roleResponseService.findByName("USER");
            user.setRoles(Collections.singleton(userRole));
        } else {
            if (userRequestDTO.getRoles() == null) {
                Role userRole = roleResponseService.findByName("USER");
                user.setRoles(Collections.singleton(userRole));
            } else {
                Set<Role> roles = userRequestDTO.getRoles().stream()
                        .map(roleResponseService::findByName)
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
        User existingUser = findAndValidateUser(id);

        applyPartialUpdate(dto, existingUser);
        Set<Role> roles = validateUpdateRoleUser(dto);
        if (roles != null) existingUser.setRoles(roles);

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findAndValidateUser(id);
        addressRepository.deleteAllByUser_Id(id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        User user = findAndValidateUser(id);
        if (!user.isActive()) return;
        addressRepository.softDeleteByUserId(id);
        userRepository.softDeleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends User> iterable) {
        iterable.forEach(user -> {
            findAndValidateUser(user.getId());
            addressRepository.deleteAllByUser_Id(user.getId());
            userRepository.deleteById(user.getId());
        });
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        String currentPassword = authService.getAuthenticatedUser().getPassword();
        if (!bCryptPasswordEncoder.matches(dto.getCurrentPassword(), currentPassword))
            throw new IncorrectPasswordException(ErrorCode.USER_PASSWORD_CURRENT_INCORRECT);
        if (bCryptPasswordEncoder.matches(dto.getNewPassword(), currentPassword))
            throw new IncorrectPasswordException(ErrorCode.PASSWORD_SAME);
        if (!dto.getNewPassword().equals(dto.getConfirmPassword()))
            throw new IncorrectPasswordException(ErrorCode.USER_PASSWORD_CONFIRM_MISMATCH);
        User user = authService.getAuthenticatedUser();
        user.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
