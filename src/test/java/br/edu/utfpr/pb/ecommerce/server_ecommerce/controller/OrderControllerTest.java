package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.dto.AddressCEP;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.service.CepService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.CompanyResponse;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.PackageResponse;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class OrderControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/orders";

    @Mock
    private CepService cepService;
    @Mock
    private MelhorEnvioService melhorEnvioService;

    @Test
    public void postOrder_whenUserAuthenticated_thenCreateOrder() {
        String userToken = authenticateAsUser();

        List<PackageResponse>  packages = new ArrayList<>();
        // 1. Configurar o Mock do Frete
        // Quando o service chamar o calculo, retornamos um frete falso válido
        ShipmentResponseDTO mockShipment = new ShipmentResponseDTO(
                123L, // ID que vamos mandar no JSON
                "SEDEX Mock",
                (BigDecimal.valueOf(20.00)),
                (BigDecimal.valueOf(20.00)),
                (BigDecimal.valueOf(0.00)) ,
                "BRL",
                3,
                packages,
                new CompanyResponse(1L, "Correios", "url"),
                null
        );

        // 1. Configura o comportamento do mock do CepService
        AddressCEP mockAddressCep = new AddressCEP("85501000", "PR", "Pato Branco", "Centro", "Rua Teste");
        Mockito.when(cepService.getAddressByCEP("85501000")).thenReturn(mockAddressCep);
        Mockito.when(melhorEnvioService.calculateShipmentByProducts(any(ShipmentRequestDTO.class)))
                .thenReturn(List.of(mockShipment));

//        // 2. Cria o DTO de requisição do pedido
        AddressRequestDTO addressDTO = new AddressRequestDTO("12", null, "85501000");
        OrderRequestDTO newOrder = OrderRequestDTO.builder()
                .paymentId(1L) // PIX do test-data.sql
                .address(addressDTO)
                .shipmentId(1L) // ID do frete (simulando a escolha do usuário)
                .orderItems(List.of(
                        OrderItemRequestDTO.builder().productId(1L).quantity(1).build() // Fender do test-data.sql
                ))
                .build();

        // 3. Executa a requisição
        ResponseEntity<OrderResponseDTO> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newOrder, userToken),
                OrderResponseDTO.class
        );

        // 4. Valida a resposta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getOrderItems()).hasSize(1);
        assertThat(response.getBody().getAddress().getStreet()).isEqualTo("Avenida Tupi"); // Verifica se o endereço do mock foi usado
        assertThat(response.getBody().getStatus()).isEqualTo("PENDENTE"); // Verifica se o endereço do mock foi usado
    }
}
