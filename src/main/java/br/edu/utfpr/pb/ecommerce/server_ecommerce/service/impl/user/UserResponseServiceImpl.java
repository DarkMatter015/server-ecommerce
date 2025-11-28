package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.UserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.isAdmin;

@Service
public class UserResponseServiceImpl extends CrudResponseServiceImpl<User, Long> implements IUserResponseService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserResponseServiceImpl(UserRepository userRepository, UserRepository userRepository1, AuthService authService) {
        super(userRepository);
        this.userRepository = userRepository1;
        this.authService = authService;
    }

    @Override
    public List<User> findAll() {
        User authenticatedUser = authService.getAuthenticatedUser();
        if (isAdmin(authenticatedUser)) {
            return userRepository.findAll();
        }
        return userRepository.findById(authenticatedUser.getId()).stream().toList();
    }

    @Override
    public List<User> findAll(Sort sort) {
        User authenticatedUser = authService.getAuthenticatedUser();
        if (isAdmin(authenticatedUser)) {
            return userRepository.findAll(sort);
        }
        return userRepository.findById(authenticatedUser.getId()).stream().toList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        User authenticatedUser = authService.getAuthenticatedUser();
        if (isAdmin(authenticatedUser)) {
            return userRepository.findAll(pageable);
        }
        return (Page<User>) userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with this id: " + authenticatedUser.getId()));
    }

    @Override
    public User findById(Long aLong) {
        User authenticatedUser = authService.getAuthenticatedUser();
        if (!isAdmin(authenticatedUser) && !authenticatedUser.getId().equals(aLong)) {
            throw new UserNotFoundException("User not found.");
        }
        return userRepository.findById(aLong).orElseThrow(() -> new UserNotFoundException("User not found with this id: " + aLong));
    }
}
