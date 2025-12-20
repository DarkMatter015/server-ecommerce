package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.ReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IAddress.IAddressResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("addresses")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Address Read", description = "Endpoints for reading addresses")
public class ReadAddressController extends ReadController<Address, AddressResponseDTO, Long> {

    public ReadAddressController(IAddressResponseService addressResponseService, ModelMapper modelMapper) {
        super(AddressResponseDTO.class, addressResponseService, modelMapper);
    }
}
