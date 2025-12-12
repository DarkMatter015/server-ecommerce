package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/categories";

    @Test
    public void postCategory_whenAdmin_thenCreateCategory() {
        String adminToken = authenticateAndGetToken("admin@teste.com");
        Category newCategory = Category.builder().name("New Test Category").build();

        ResponseEntity<Category> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newCategory, adminToken),
                Category.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Test Category");
    }

    @Test
    public void getCategories_whenCalled_thenReturnListOfCategories() {
        ResponseEntity<Category[]> response = testRestTemplate.getForEntity(API_URL, Category[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void deleteCategory_whenAdmin_thenSoftDeleteCategory() {
        String adminToken = authenticateAndGetToken("admin@teste.com");

        // Categoria 1 (Violões) existe no V2__Populate_Data.sql
        ResponseEntity<Void> response = testRestTemplate.exchange(
                API_URL + "/1",
                HttpMethod.DELETE,
                getRequestEntity(adminToken),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Tenta buscar a categoria deletada (deve dar 404 por causa do @Where)
        ResponseEntity<Object> findResponse = testRestTemplate.getForEntity(API_URL + "/1", Object.class);
        assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
