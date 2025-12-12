package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/orders";

    @Test
    public void postOrder_whenUserAuthenticated_thenCreateOrder() {
        String userToken = authenticateAndGetToken("test@teste.com");

        AddressRequestDTO newAddress = AddressRequestDTO.builder().cep("85501000").number("123").build();

        OrderRequestDTO newOrder = OrderRequestDTO.builder()
                .paymentId(1L) // PIX
                .shipmentId(1L)
                .address(newAddress) // Endereço do usuário 'test'
                .orderItems(List.of(
                        OrderItemRequestDTO.builder().productId(1L).quantity(1).build() // Guitarra Fender
                ))
                .build();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newOrder, userToken),
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
//        assertThat(response.getBody().getId()).isNotNull();
//        assertThat(response.getBody().getOrderItems()).hasSize(1);
    }

    @Test
    public void getOrders_whenUser_thenReturnOnlyOwnOrders() {
        String userToken = authenticateAndGetToken("test@teste.com");

        ResponseEntity<RestResponsePage<Order>> response = testRestTemplate.exchange(
                API_URL + "page",
                HttpMethod.GET,
                getRequestEntity(userToken),
                new ParameterizedTypeReference<RestResponsePage<Order>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // O usuário 'test' tem 10 pedidos no V2__Populate_Data.sql
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getTotalElements()).isEqualTo(10);
    }
}
