package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.IBrasilAPI;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.exception.CepException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepService {

    private final IBrasilAPI brasilApi;

    public AddressCEP getAddressByCEP(String cep) {
        try {
            return brasilApi.getAddressByCEP(cep);
        } catch (FeignException e) {
            throw new CepException("CEP not found or invalid.");
        }
    }
}
