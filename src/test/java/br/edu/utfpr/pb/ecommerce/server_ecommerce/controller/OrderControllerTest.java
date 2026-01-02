package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order.OrderPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/orders";

    @MockitoBean
    private OrderPublisher orderPublisher;
    @Autowired
    private TestUtils utils;

    @Test
    public void postOrder_whenUserAuthenticated_thenCreateOrder() {
//        1. PREPARAÇÃO
        String userToken = authenticateAsUser();

        Product product = utils.createProductWithStock(1);
        Payment payment = utils.createPayment();
        OrderRequestDTO request = utils.createOrderRequest(product.getId(), payment.getId());

        // 2. EXECUÇÃO
        ResponseEntity<OrderResponseDTO> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(request, userToken),
                OrderResponseDTO.class
        );

        System.out.println(response.getBody());

        // 3. VALIDAÇÃO
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getOrderItems()).hasSize(1);
        assertThat(response.getBody().getAddress().getStreet()).isEqualTo(null);
        assertThat(response.getBody().getShipment().getId()).isEqualTo(1L);
        assertThat(response.getBody().getShipment().getName()).isEqualTo(null);
        assertThat(response.getBody().getStatus()).isEqualTo("PROCESSANDO");
    }
}
