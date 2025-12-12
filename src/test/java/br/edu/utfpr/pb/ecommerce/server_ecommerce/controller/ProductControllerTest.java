package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/products";

    @Test
    public void postProduct_whenAdmin_thenCreateProduct() {
        String adminToken = authenticateAndGetToken("admin@teste.com");
        Category category = new Category("Violões"); // Categoria 'Violões'
        Product newProduct = Product.builder()
                .name("New Test Product")
                .price(new BigDecimal("99.99"))
                .quantityAvailableInStock(10)
                .category(category)
                .build();

        ResponseEntity<Product> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newProduct, adminToken),
                Product.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Test Product");
    }

    @Test
    public void postProduct_whenUser_thenForbidden() {
        String userToken = authenticateAndGetToken("test@teste.com");
        Product newProduct = Product.builder().name("Attempt").build();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newProduct, userToken),
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getProducts_whenCalled_thenReturnListOfProducts() {
        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(API_URL, Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
