package br.edu.utfpr.pb.ecommerce.server_ecommerce;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.CompanyResponse;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.PackageResponse;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class APITest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    // MOCK IMPORTANTE: Evita chamar a API real do Melhor Envio durante os testes
    @MockitoBean
    private MelhorEnvioService melhorEnvioService;

    @MockitoBean
    private br.edu.utfpr.pb.ecommerce.server_ecommerce.rabbitmq.publisher.OrderPublisher orderPublisher;

    @BeforeEach
    public void setup() {
        // Garante que o usuário do import.sql existe (ou recria se o banco for h2 in-memory limpo)
        if (userRepository.findByEmail("admin@teste.com") == null) {
            // Lógica de fallback caso o import.sql falhe ou não rode nos testes
        }
    }

    private String obtainAccessToken(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            UserRequestDTO newUser = new UserRequestDTO();
            newUser.setDisplayName("Integration Test User");
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setCpf("12345678901");
            newUser.setRoles(java.util.Set.of("USER"));
            testRestTemplate.postForEntity("/users", newUser, Object.class);
        }

        var loginRequest = Map.of(
                "email", email,
                "password", password
        );

        ResponseEntity<Map> response = testRestTemplate
                .postForEntity("/auth/login", loginRequest, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return (String) response.getBody().get("token");
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // --- Categoria ---
    @Test
    public void getCategories_whenCalled_receiveList() {
        ResponseEntity<Object[]> response =
                testRestTemplate.getForEntity("/categories", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    // --- Produtos ---
    @Test
    public void getProducts_whenCalled_receiveList() {
        ResponseEntity<Object[]> response =
                testRestTemplate.getForEntity("/products", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void getProductById_whenIdExists_receiveProduct() {
        ResponseEntity<Object> response =
                testRestTemplate.getForEntity("/products/1", Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    // --- Usuário ---
    @Test
    public void postUser_whenUserIsValid_receiveCREATED() {
        UserRequestDTO user = new UserRequestDTO();
        user.setDisplayName("Integration Test User 2");
        user.setEmail("integration2@teste.com");
        user.setPassword("P4ssword1A");
        user.setCpf("12345678902");
        user.setRoles(java.util.Set.of("USER"));

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity("/users", user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    // --- Endereço ---
    @Test
    public void postAddress_whenUserAuthenticated_addressSaved() {
        String token = obtainAccessToken("integration@teste.com", "P4ssword1A");

        AddressRequestDTO address = new AddressRequestDTO();
        address.setCep("85501000");
        address.setNumber("123");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(
                        "/addresses",
                        new HttpEntity<>(address, createHeaders(token)),
                        Object.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    // --- Pedido ---
    @Test
    public void postOrder_whenUserAuthenticated_orderSaved() {
        String token = obtainAccessToken("integration@teste.com", "P4ssword1A");

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

        Mockito.when(melhorEnvioService.calculateShipmentByProducts(any(ShipmentRequestDTO.class)))
                .thenReturn(List.of(mockShipment));

        // 2. Criar JSON com shipmentId combinando com o Mock
        String orderJson = """
                {
                    "orderItems": [
                        {
                        "productId": 1,
                        "quantity": 1
                        }
                    ],
                    "address": {
                        "number": "573",
                        "cep":  "85503359"
                    },
                    "paymentId": 1,
                    "shipmentId": 123
                }
                """;

        ResponseEntity<OrderResponseDTO> response =
                testRestTemplate
                        .postForEntity(
                                "/orders",
                                new HttpEntity<>(orderJson, createHeaders(token)),
                                OrderResponseDTO.class
                        );

        // Debug caso falhe
        if (response.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Erro no teste de Pedido: " + response.getBody());
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        // Opcional: Verificar se o frete foi gravado
         assertThat(response.getBody().getShipment().id()).isEqualTo(123);
        assertThat(response.getBody().getStatus()).isEqualTo("PENDING");
    }

    @Test
    public void getOrders_whenUserAuthenticated_receiveOnlyUserOrders() {
        String token = obtainAccessToken("integration@teste.com", "P4ssword1A");
        HttpHeaders headers = createHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = testRestTemplate.exchange(
                "/orders",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}