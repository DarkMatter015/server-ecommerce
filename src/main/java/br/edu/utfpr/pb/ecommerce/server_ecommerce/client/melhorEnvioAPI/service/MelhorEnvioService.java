package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.IMelhorEnvioAPI;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.PostalCodeRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MelhorEnvioService {

    private final IMelhorEnvioAPI melhorEnvioApi;

    @Value("${ORIGIN_CEP}")
    private String ORIGIN_CEP;

    public List<ShipmentResponseDTO> calculateShipmentByProducts(ShipmentRequestDTO shipmentRequestDto) {
        shipmentRequestDto.setFrom(new PostalCodeRequest(ORIGIN_CEP));
        return melhorEnvioApi.calculateFreightByProducts(shipmentRequestDto).stream()
                .filter(s -> s.error() == null || s.error().isBlank())
                .sorted(Comparator.comparing(ShipmentResponseDTO::price))
                .limit(4)
                .toList();
    }
}
