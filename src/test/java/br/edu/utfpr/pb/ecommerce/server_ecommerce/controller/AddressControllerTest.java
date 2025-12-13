package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressResponseDTO;
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
        String userToken = authenticateAsUser();

        AddressRequestDTO newAddress = AddressRequestDTO.builder()
                .cep("85501000")
                .number("456")
                .build();

        ResponseEntity<AddressResponseDTO> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newAddress, userToken),
                AddressResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getCep()).isEqualTo("85501000");
    }

    @Test
    public void deleteAddress_whenUserDoesNotOwnAddress_thenForbidden() {
        // Tenta deletar o endereço de outro usuário (ID 1 pertence ao user@teste.com)
        String adminToken = authenticateAsAdmin();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_URL + "/1", // Endereço do user@teste.com
                HttpMethod.DELETE,
                getRequestEntity(adminToken), // Admin tentando deletar
                Object.class
        );

        // A regra de negócio atual impede que até o admin delete o endereço de outro usuário
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
