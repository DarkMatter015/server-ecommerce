package br.edu.utfpr.pb.ecommerce.server_ecommerce.utils;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address.AddressRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderItemDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.*;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class TestUtils {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public Product createProductWithStock(int quantity) {
        Category category = categoryRepository.save(new Category("Categoria Teste"));

        // Garante que o status existe
        if (orderStatusRepository.findByName("PROCESSANDO").isEmpty()) {
            orderStatusRepository.save(new OrderStatus("PROCESSANDO"));
        }

        Product product = new Product();
        product.setName("Produto Teste Concorrência");
        product.setQuantityAvailableInStock(quantity);
        product.setVersion(0L);
        product.setCategory(category);
        product.setPrice(BigDecimal.TEN);
        return productRepository.save(product);
    }

    public User createValidUser() {
        Role role = roleRepository.save(new Role("USER_TEST"));
        User user = User.builder()
                .cpf("12345678901")
                .displayName("User Teste")
                .email("usertest" + UUID.randomUUID() + "@gmail.com") // Email único para evitar conflito em testes repetidos
                .password("SenhaForte1!")
                .roles(Collections.singleton(role))
                .build();
        return userRepository.save(user);
    }

    public Payment createPayment() {
        return paymentRepository.save(Payment.builder().name("Payment Test").build());
    }

    public OrderRequestDTO createOrderRequest(Long productId, Long paymentId) {
        OrderItemDTO itemDto = new OrderItemDTO(productId, 1);
        AddressRequestDTO address = new AddressRequestDTO("12", "Complemento", "12345678");
        return new OrderRequestDTO(List.of(itemDto), address, paymentId, 1L);
    }
}
