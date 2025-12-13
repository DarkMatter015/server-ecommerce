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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/users";

    @Test
    public void postUser_whenValidUser_thenCreateUser() {
        UserRequestDTO newUser = UserRequestDTO.builder()
                .displayName("New User")
                .email("newuser@teste.com")
                .password("P4ssword1")
                .cpf("22222222222")
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity(API_URL, newUser, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getEmail()).isEqualTo("newuser@teste.com");
    }

    @Test
    public void getUsers_whenAdmin_thenReturnAllUsers() {
        String adminToken = authenticateAsAdmin();

        ResponseEntity<List<User>> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                getRequestEntity(adminToken),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Deve encontrar os 2 usuários do test-data.sql
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void getUsers_whenUser_thenReturnOnlySelf() {
        String userToken = authenticateAsUser();

        ResponseEntity<List<User>> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                getRequestEntity(userToken),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst().getEmail()).isEqualTo("user@teste.com");
    }
}
