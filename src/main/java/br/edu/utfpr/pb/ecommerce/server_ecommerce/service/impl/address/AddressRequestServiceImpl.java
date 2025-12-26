package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address.IAddress.IAddressRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.UserResponseServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;
@Service
public class AddressRequestServiceImpl extends BaseSoftDeleteRequestServiceImpl<Address, AddressUpdateDTO> implements IAddressRequestService {

    private final AddressRepository addressRepository;
    private final AddressResponseServiceImpl addressResponseService;
    private final UserResponseServiceImpl userResponseService;
    private final AuthService  authService;
    private final ModelMapper modelMapper;
    private final CepService cepService;

    public AddressRequestServiceImpl(AddressRepository addressRepository, AddressResponseServiceImpl addressResponseService, UserResponseServiceImpl userResponseService, AuthService authService, ModelMapper modelMapper, CepService cepService) {
        super(addressRepository, addressResponseService);
        this.addressRepository = addressRepository;
        this.addressResponseService = addressResponseService;
        this.userResponseService = userResponseService;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.cepService = cepService;
    }

    private void validateAddressOwnership(Address address) {
        User user = authService.getAuthenticatedUser();
        if (!address.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ADDRESS_PERMISSION_MODIFY_DENIED);
        }
    }

    private void findAndValidateAddress(Long id, User user) {
        addressRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException(Address.class, id));
    }

    @Override
    @Transactional
    public Address activate(Long id) {
        Address address = addressResponseService.findById(id);
        if (address.isActive()) return address;
        User user = userResponseService.findById(address.getUser().getId());
        if (!user.isActive()) throw new BusinessException(ErrorCode.ADDRESS_USER_ACTIVATE_REQUIRED, user.getEmail(), user.getId());
        address.setDeletedAt(null);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address createAddress(AddressRequestDTO addressDTO) {

        AddressCEP cepData = cepService.getAddressByCEP(addressDTO.getCep());

        Address address = map(cepData, Address.class, modelMapper);
        address.setNumber(addressDTO.getNumber());
        address.setComplement(addressDTO.getComplement());
        User user = authService.getAuthenticatedUser();
        address.setUser(user);

        return super.save(address);
    }

    @Override
    @Transactional
    public Address save(Address address) {
        validateAddressOwnership(address);
        return super.save(address);
    }

    @Override
    @Transactional
    public Iterable<Address> save(Iterable<Address> iterable) {
        StreamSupport.stream(iterable.spliterator(), false)
                .forEach(this::validateAddressOwnership);
        return super.save(iterable);
    }

    @Override
    @Transactional
    public Address saveAndFlush(Address address) {
        validateAddressOwnership(address);
        return super.saveAndFlush(address);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = authService.getAuthenticatedUser();
        findAndValidateAddress(id, user);
        super.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Address> iterable) {
        iterable.forEach(this::validateAddressOwnership);
        super.delete(iterable);
    }
}
