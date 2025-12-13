package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.BaseIntegrationTest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.CategoryNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductControllerTest extends BaseIntegrationTest {

    private static final String API_URL = "/products";
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void postProduct_whenAdmin_thenCreateProduct() {
        String adminToken = authenticateAsAdmin();
        Optional<Category> category = categoryRepository.findById(1L);
        if (category.isPresent()) {
            ProductRequestDTO newProduct = ProductRequestDTO.builder()
                    .name("New Test Product")
                    .price(new BigDecimal("99.99"))
                    .quantityAvailableInStock(10)
                    .categoryId(category.get().getId())
                    .build();

            ResponseEntity<ProductResponseDTO> response = testRestTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    getRequestEntity(newProduct, adminToken),
                    ProductResponseDTO.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            Assertions.assertNotNull(response.getBody());
            assertThat(response.getBody().getName()).isEqualTo("New Test Product");
        } else throw new CategoryNotFoundException("Category not found with ID 1");
    }

    @Test
    public void postProduct_whenUser_thenForbidden() {
        String userToken = authenticateAsUser();
        Optional<Category> category = categoryRepository.findById(1L);
        if (category.isPresent()) {
            Product newProduct = Product.builder().name("Attempt").price(BigDecimal.ONE).quantityAvailableInStock(1).category(category.get()).build();

            ResponseEntity<Object> response = testRestTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    getRequestEntity(newProduct, userToken),
                    Object.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        } else throw new CategoryNotFoundException("Category not found with ID 1");
    }
}
