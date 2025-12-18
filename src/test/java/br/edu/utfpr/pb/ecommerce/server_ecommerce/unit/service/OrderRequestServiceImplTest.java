package br.edu.utfpr.pb.ecommerce.server_ecommerce.unit.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.dto.order.OrderEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderStatusNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.publisher.order.OrderPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderStatusRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRequestServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AuthService authService;
    @Mock
    private OrderMapper orderMapper; // Using mock instead of real mapper to isolate
    @Mock
    private OrderPublisher orderPublisher;
    @Mock
    private OrderStatusRepository orderStatusRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderRequestServiceImpl orderRequestService;

    @Test
    @DisplayName("Should create order successfully when stock is valid")
    void createOrderSuccess() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setCpf("12345678901");

        Product product = new Product();
        product.setId(1L);
        product.setQuantityAvailableInStock(10);
        product.setPrice(BigDecimal.TEN);

        OrderStatus status = new OrderStatus();
        status.setName("PROCESSANDO");

        OrderRequestDTO request = OrderRequestDTO.builder()
                .orderItems(List.of(
                        OrderItemRequestDTO.builder().productId(1L).quantity(1).build()
                ))
                .build();

        Order orderEntity = new Order();
        orderEntity.setId(1L);

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findAllById(anySet())).thenReturn(List.of(product));
        when(orderStatusRepository.findByName("PROCESSANDO")).thenReturn(Optional.of(status));
        when(orderMapper.toEntity(request)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.toEventDTO(orderEntity, user.getCpf())).thenReturn(mock(OrderEventDTO.class));

        // Act
        Order result = orderRequestService.createOrder(request);

        // Assert
        assertThat(result).isNotNull();
        verify(orderRepository).save(orderEntity);
        verify(orderPublisher).send(any(OrderEventDTO.class));
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product does not exist")
    void createOrderProductNotFound() {
        // Arrange
        User user = new User();

        OrderRequestDTO request = OrderRequestDTO.builder()
                .orderItems(List.of(
                        OrderItemRequestDTO.builder().productId(99L).quantity(1).build()
                ))
                .build();

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findAllById(anySet())).thenReturn(List.of());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> orderRequestService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidQuantityException when stock is insufficient")
    void createOrderInsufficientStock() {
        // Arrange
        User user = new User();
        Product product = new Product();
        product.setId(1L);
        product.setQuantityAvailableInStock(5);

        OrderRequestDTO request = OrderRequestDTO.builder()
                .orderItems(List.of(
                        OrderItemRequestDTO.builder().productId(1L).quantity(10).build()
                ))
                .build();

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findAllById(anySet())).thenReturn(List.of(product));

        // Act & Assert
        assertThrows(InvalidQuantityException.class, () -> orderRequestService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }
}
