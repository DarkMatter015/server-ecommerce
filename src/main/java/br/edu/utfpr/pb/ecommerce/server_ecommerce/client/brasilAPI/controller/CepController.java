package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.controller.iCepController.ICepController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
public class CepController implements ICepController {

    private final CepService cepService;

    @Override
    @GetMapping("/validate/{cep}")
    public ResponseEntity<AddressCEP> getAddressByCEP(@PathVariable String cep) {
        AddressCEP address = cepService.getAddressByCEP(cep);
        return ResponseEntity.ok(address);
    }
}
