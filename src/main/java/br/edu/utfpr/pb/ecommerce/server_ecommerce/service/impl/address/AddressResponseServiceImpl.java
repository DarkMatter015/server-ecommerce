package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.AddressNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.FilterManager;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IAddress.IAddressResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressResponseServiceImpl extends CrudResponseServiceImpl<Address, Long> implements IAddressResponseService {

    private final AddressRepository addressRepository;
    private final AuthService authService;

    public AddressResponseServiceImpl(AddressRepository addressRepository, FilterManager filterManager, AuthService authService) {
        super(addressRepository, filterManager, authService);
        this.addressRepository = addressRepository;
        this.authService = authService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAll() {
        User user = authService.getAuthenticatedUser();
        return addressRepository.findAllByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAll(Sort sort) {
        User user = authService.getAuthenticatedUser();
        return addressRepository.findAllByUser(user, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        User user = authService.getAuthenticatedUser();
        return addressRepository.findAllByUser(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Address findById(Long id) {
        User user = authService.getAuthenticatedUser();
        return addressRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        User user = authService.getAuthenticatedUser();
        return addressRepository.existsByUserAndId(user, id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        User user = authService.getAuthenticatedUser();
        return addressRepository.countByUser(user);
    }
}
