package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.address.EmbeddedAddress;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public String toAddressInfo(EmbeddedAddress address) {
        if (address == null) return "";
        return address.getStreet() != null ? address.getStreet() : "Rua"
                + ", " + address.getNumber()
                + " - CEP: " + address.getCep()
                + " - " + address.getComplement()
                + (address.getNeighborhood() != null ? " - " + address.getNeighborhood() : "")
                + (address.getCity() != null ? " - " + address.getCity() : "")
                + (address.getState() != null ? " - " + address.getState() : "");
    }
}

