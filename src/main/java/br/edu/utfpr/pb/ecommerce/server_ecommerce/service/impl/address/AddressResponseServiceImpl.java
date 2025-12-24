package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address.IAddress.IAddressResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteResponseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressResponseServiceImpl extends BaseSoftDeleteResponseServiceImpl<Address, Long> implements IAddressResponseService {

    public AddressResponseServiceImpl(AddressRepository addressRepository, AuthService authService) {
        super(addressRepository, authService);
    }
}
