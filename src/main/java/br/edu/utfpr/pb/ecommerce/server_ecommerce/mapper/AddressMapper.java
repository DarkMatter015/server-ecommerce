package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.EmbeddedAddress;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    private final ModelMapper modelMapper;

    public EmbeddedAddress toEmbeddedAddress(AddressCEP addressCEP, AddressRequestDTO addressRequestDTO) {
        EmbeddedAddress embeddedAddress = map(addressCEP, EmbeddedAddress.class, modelMapper);
        embeddedAddress.setNumber(addressRequestDTO.getNumber());
        embeddedAddress.setComplement(addressRequestDTO.getComplement());
        return embeddedAddress;
    }

    public AddressRequestDTO toRequestDTO(EmbeddedAddress embeddedAddress) {
        return modelMapper.map(embeddedAddress, AddressRequestDTO.class);
    }
}
