package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.UserNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.FilterManager;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.isAuthenticatedAndAdmin;

@Service
public class UserResponseServiceImpl extends CrudResponseServiceImpl<User, Long> implements IUserResponseService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final FilterManager filterManager;

    public UserResponseServiceImpl(UserRepository userRepository, FilterManager filterManager, AuthService authService) {
        super(userRepository, filterManager, authService);
        this.userRepository = userRepository;
        this.authService = authService;
        this.filterManager = filterManager;
    }

    private boolean validateAdminAndUpdateFilter() {
        if (isAuthenticatedAndAdmin(authService)) {
            filterManager.disableActiveFilter();
            return true;
        }
        filterManager.enableActiveFilter(true);
        return false;
    }

    @Override
    public List<User> findAll() {
        if (validateAdminAndUpdateFilter())
            return userRepository.findAll();

        User authenticatedUser = authService.getAuthenticatedUser();
        return userRepository.findById(authenticatedUser.getId()).stream().toList();
    }

    @Override
    public List<User> findAll(Sort sort) {
        if (validateAdminAndUpdateFilter())
            return userRepository.findAll(sort);

        User authenticatedUser = authService.getAuthenticatedUser();
        return userRepository.findById(authenticatedUser.getId()).stream().toList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        if (validateAdminAndUpdateFilter())
            return userRepository.findAll(pageable);

        User authenticatedUser = authService.getAuthenticatedUser();
        return (Page<User>) userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with this id: " + authenticatedUser.getId()));
    }

    @Override
    public User findById(Long aLong) {
        User authenticatedUser = authService.getAuthenticatedUser();
        if (!validateAdminAndUpdateFilter() && !authenticatedUser.getId().equals(aLong))
            throw new UserNotFoundException("User not found.");

        return userRepository.findById(aLong).orElseThrow(() -> new UserNotFoundException("User not found with this id: " + aLong));
    }
}
