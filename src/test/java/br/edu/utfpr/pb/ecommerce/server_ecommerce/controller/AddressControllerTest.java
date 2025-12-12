package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/addresses";

    @Test
    public void postAddress_whenUserAuthenticated_thenCreateAddress() {
        String userToken = authenticateAndGetToken("test@teste.com");

        AddressRequestDTO newAddress = AddressRequestDTO.builder()
                .cep("85501000")
                .number("123")
                .build();

        ResponseEntity<Address> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newAddress, userToken),
                Address.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getCep()).isEqualTo("85501000");
    }

    @Test
    public void deleteAddress_whenUserOwnsAddress_thenSoftDelete() {
        String userToken = authenticateAndGetToken("test@teste.com");

        // Endereço 3 pertence ao usuário 'test' no V2__Populate_Data.sql
        ResponseEntity<Void> response = testRestTemplate.exchange(
                API_URL + "/3",
                HttpMethod.DELETE,
                getRequestEntity(userToken),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteAddress_whenUserDoesNotOwnAddress_thenNotFound() {
        String userToken = authenticateAndGetToken("test@teste.com");

        // Endereço 1 pertence ao usuário 'admin'
        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_URL + "/1",
                HttpMethod.DELETE,
                getRequestEntity(userToken),
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
