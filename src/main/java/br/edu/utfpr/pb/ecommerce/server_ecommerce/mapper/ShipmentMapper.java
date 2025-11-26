package br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment.EmbeddedCompanyDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment.EmbeddedShipmentDTO;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    public EmbeddedShipmentDTO toEmbedded(ShipmentResponseDTO response) {
        if (response == null) return null;

        return new EmbeddedShipmentDTO(
                response.id(),
                response.name(),
                response.price(),
                response.custom_price(),
                response.discount(),
                response.currency(),
                response.delivery_time(),
                new EmbeddedCompanyDTO(
                        response.company().name(),
                        response.company().picture()
                )
        );
    }
}
