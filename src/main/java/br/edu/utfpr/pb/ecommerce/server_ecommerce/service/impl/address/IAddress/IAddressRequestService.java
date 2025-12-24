package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address.IAddress;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface IAddressRequestService extends IBaseSoftDeleteRequestService<Address, AddressUpdateDTO, Long> {
    Address createAddress(AddressRequestDTO addressDTO);
}
