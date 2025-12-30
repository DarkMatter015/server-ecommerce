package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.IUser.IUserResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteResponseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.AuthValidation.isAuthenticatedAndAdmin;

@Service
public class UserResponseServiceImpl extends BaseSoftDeleteResponseServiceImpl<User, Long> implements IUserResponseService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserResponseServiceImpl(UserRepository userRepository, AuthService authService) {
        super(userRepository, authService);
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public List<User> findAll() {
        if (isAuthenticatedAndAdmin())
            return userRepository.findAll();

        User authenticatedUser = authService.getAuthenticatedUser();
        return userRepository.findById(authenticatedUser.getId()).stream().toList();
    }

    @Override
    public List<User> findAll(Sort sort) {
        if (isAuthenticatedAndAdmin())
            return userRepository.findAll(sort);

        User authenticatedUser = authService.getAuthenticatedUser();
        return userRepository.findById(authenticatedUser.getId()).stream().toList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        if (isAuthenticatedAndAdmin()) {
            return userRepository.findAll(pageable);
        }

        User authenticatedUser = authService.getAuthenticatedUser();
        List<User> userList = Collections.singletonList(authenticatedUser);
        return new PageImpl<>(userList, pageable, 1);
    }

    @Override
    public User findById(Long id) {
        User authenticatedUser = authService.getAuthenticatedUser();
        if (!isAuthenticatedAndAdmin() && !authenticatedUser.getId().equals(id))
            throw new ResourceNotFoundException(User.class, id);

        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }
}
