package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.AddressNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.exception.CepNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IAddress.IAddressRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.validateStringNullOrBlank;

@Service
public class AddressRequestServiceImpl extends CrudRequestServiceImpl<Address, AddressUpdateDTO, Long> implements IAddressRequestService {

    private final AddressRepository addressRepository;
    private final AuthService  authService;
    private final ModelMapper modelMapper;
    private final CepService cepService;

    public AddressRequestServiceImpl(AddressRepository addressRepository, AuthService authService, ModelMapper modelMapper, CepService cepService) {
        super(addressRepository);
        this.addressRepository = addressRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.cepService = cepService;
    }

    private void validateAddressOwnership(Address address) {
        User user = authService.getAuthenticatedUser();
        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to modify this address!");
        }
    }

    protected Address findAndValidateAddress(Long id, User user) {
        return addressRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found."));
    }

    @Override
    @Transactional
    public Address createAddress(AddressRequestDTO addressDTO) {

        AddressCEP cepData;
        try {
            cepData = cepService.getAddressByCEP(addressDTO.getCep());
        } catch (Exception e) {
            throw new CepNotFoundException("CEP not found or invalid.");
        }

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
    public Address update(Long id, AddressUpdateDTO updateDTO) {
        User user = authService.getAuthenticatedUser();
        Address address = findAndValidateAddress(id, user);

        if(updateDTO.getNumber() != null) {
            validateStringNullOrBlank(updateDTO.getNumber());
            address.setNumber(updateDTO.getNumber());
        }

        if(updateDTO.getComplement() != null) {
            validateStringNullOrBlank(updateDTO.getComplement());
            address.setComplement(updateDTO.getComplement());
        }

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address saveAndFlush(Address address) {
        validateAddressOwnership(address);
        return super.saveAndFlush(address);
    }

    @Override
    @Transactional
    public void deleteAll() {
        User user = authService.getAuthenticatedUser();
        addressRepository.deleteAllByUser(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = authService.getAuthenticatedUser();
        addressRepository.deleteByIdAndUser(id, user);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Address> iterable) {
        User user = authService.getAuthenticatedUser();
        List<Long> addressIds = StreamSupport.stream(iterable.spliterator(), false)
                .map(Address::getId)
                .collect(Collectors.toList());
        addressRepository.deleteAllByIdInAndUser(addressIds, user);
    }
}
