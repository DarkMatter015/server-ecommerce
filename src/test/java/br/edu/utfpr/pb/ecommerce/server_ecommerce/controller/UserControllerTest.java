package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/users";

    @Test
    public void postUser_whenValidUser_thenCreateUser() {
        UserRequestDTO newUser = UserRequestDTO.builder()
                .displayName("New User")
                .email("newuser@teste.com")
                .password("P4ssword1")
                .cpf("11122233344")
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity(API_URL, newUser, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getEmail()).isEqualTo("newuser@teste.com");
    }

    @Test
    public void getUsers_whenAdmin_thenReturnAllUsers() {
        String adminToken = authenticateAndGetToken("admin@teste.com");

        ResponseEntity<RestResponsePage<User>> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                getRequestEntity(adminToken),
                new ParameterizedTypeReference<RestResponsePage<User>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getTotalElements()).isGreaterThan(1);
    }

    @Test
    public void getUsers_whenUser_thenReturnOnlySelf() {
        String userToken = authenticateAndGetToken("test@teste.com");

        ResponseEntity<RestResponsePage<User>> response = testRestTemplate.exchange(
                API_URL + "page",
                HttpMethod.GET,
                getRequestEntity(userToken),
                new ParameterizedTypeReference<RestResponsePage<User>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().getFirst().getEmail()).isEqualTo("test@teste.com");
    }

    @Test
    public void deleteUser_whenAdmin_thenSoftDeleteUser() {
        String adminToken = authenticateAndGetToken("admin@teste.com");

        // Usuário 2 (test@teste.com) existe no V2__Populate_Data.sql
        ResponseEntity<Void> response = testRestTemplate.exchange(
                API_URL + "/2",
                HttpMethod.DELETE,
                getRequestEntity(adminToken),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Tenta buscar o usuário deletado (deve dar 404)
        ResponseEntity<User> findResponse = testRestTemplate.exchange(
                API_URL + "/2",
                HttpMethod.GET,
                getRequestEntity(adminToken),
                User.class
        );
        assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
