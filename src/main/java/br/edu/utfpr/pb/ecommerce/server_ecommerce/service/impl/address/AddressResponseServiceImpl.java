package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AddressRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IAddress.IAddressResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressResponseServiceImpl extends CrudResponseServiceImpl<Address, Long> implements IAddressResponseService {

    public AddressResponseServiceImpl(AddressRepository addressRepository, AuthService authService) {
        super(addressRepository, authService);
    }
}
