package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.address.iAddressController.IReadAddressController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address.IAddress.IAddressResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("addresses")
public class ReadAddressController extends BaseSoftDeleteReadController<Address, AddressResponseDTO> implements IReadAddressController {

    public ReadAddressController(IAddressResponseService addressResponseService, ModelMapper modelMapper) {
        super(AddressResponseDTO.class, addressResponseService, modelMapper);
    }
}
