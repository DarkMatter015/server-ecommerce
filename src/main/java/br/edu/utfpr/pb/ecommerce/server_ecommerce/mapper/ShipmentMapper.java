package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment.EmbeddedCompanyDetails;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment.EmbeddedShipmentDetails;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    public EmbeddedShipmentDetails toEmbedded(ShipmentResponseDTO response) {
        if (response == null) return null;

        return EmbeddedShipmentDetails.builder()
                .id(response.id())
                .name(response.name())
                .price(response.price())
                .custom_price(response.custom_price())
                .discount(response.discount())
                .currency(response.currency())
                .delivery_time(response.delivery_time())
                .company(EmbeddedCompanyDetails.builder()
                        .name(response.company().name())
                        .picture(response.company().picture())
                        .build())
                .build();
    }
}
