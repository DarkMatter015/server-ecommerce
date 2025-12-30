package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.address.iAddressController.IWriteAddressController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.address.IAddress.IAddressRequestService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;

@RestController
@RequestMapping("addresses")
public class WriteAddressController extends BaseSoftDeleteWriteController<Address, AddressRequestDTO, AddressResponseDTO, AddressUpdateDTO> implements IWriteAddressController {
    private final IAddressRequestService  addressRequestService;

    public WriteAddressController(IAddressRequestService addressRequestService, ModelMapper modelMapper) {
        super(addressRequestService, modelMapper, Address.class, AddressResponseDTO.class);
        this.addressRequestService = addressRequestService;
    }

    @Override
    public ResponseEntity<AddressResponseDTO> create(@RequestBody @Valid AddressRequestDTO address) {
        Address savedAddress = addressRequestService.createAddress(address);
        return ResponseEntity.created(createUri(savedAddress)).body(convertToResponseDto(savedAddress));
    }
}
