package br.edu.utfpr.pb.ecommerce.server_ecommerce;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.dto.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"/db/clean.sql", "/db/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected String authenticateAsAdmin() {
        return authenticateAndGetToken("admin@teste.com");
    }

    protected String authenticateAsUser() {
        return authenticateAndGetToken("user@teste.com");
    }

    private String authenticateAndGetToken(String email) {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail(email);
        loginRequest.setPassword("123456");

        ResponseEntity<Map> response = testRestTemplate.postForEntity("/auth/login", loginRequest, Map.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assert response.getBody() != null;
        return (String) response.getBody().get("token");
    }

    protected HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected HttpEntity<Object> getRequestEntity(Object body, String token) {
        return new HttpEntity<>(body, getHeaders(token));
    }

    protected HttpEntity<Object> getRequestEntity(String token) {
        return new HttpEntity<>(getHeaders(token));
    }
}
