package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/categories";

    @Test
    public void postCategory_whenAdmin_thenCreateCategory() {
        String adminToken = authenticateAsAdmin();
        Category newCategory = Category.builder().name("New Test Category").build();

        ResponseEntity<CategoryResponseDTO> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                getRequestEntity(newCategory, adminToken),
                CategoryResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getName()).isEqualTo("New Test Category");
    }

    @Test
    public void getCategories_whenCalled_thenReturnListOfCategories() {
        ResponseEntity<List<CategoryResponseDTO>> response = testRestTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Deve encontrar a categoria 'Guitarras' do test-data.sql
        assertThat(response.getBody()).anyMatch(cat -> cat.getName().equals("Guitarras"));
    }

    @Test
    public void deleteCategory_whenAdmin_thenSoftDeleteCategory() {
        String adminToken = authenticateAsAdmin();

        // Categoria 1 ('Guitarras') existe no test-data.sql
        ResponseEntity<Void> response = testRestTemplate.exchange(
                API_URL + "/inactivate/1",
                HttpMethod.DELETE,
                getRequestEntity(adminToken),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Categoria 1 ('Guitarras') existe no test-data.sql
        ResponseEntity<CategoryResponseDTO> responseGet = testRestTemplate.exchange(
                API_URL + "/1",
                HttpMethod.GET,
                getRequestEntity(adminToken),
                CategoryResponseDTO.class
        );

        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(responseGet.getBody());
        assertThat(responseGet.getBody().getActive().equals(false));
    }
}
