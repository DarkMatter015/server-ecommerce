package br.edu.utfpr.pb.ecommerce.server_ecommerce.unit.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.order.WriteOrderController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteOrderControllerUnitTest {

    @Mock
    private OrderRequestServiceImpl orderRequestService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private WriteOrderController writeOrderController;

    @Test
    @DisplayName("Should create order successfully and return 201 Created")
    void createOrderSuccess() {
        // Setup mock request context for ServletUriComponentsBuilder
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/orders");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(1L);

        when(orderRequestService.createOrder(any(OrderRequestDTO.class))).thenReturn(savedOrder);
        when(orderMapper.toDTO(savedOrder)).thenReturn(responseDTO);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Act
        ResponseEntity<OrderResponseDTO> response = writeOrderController.createOrder(requestDTO, uriBuilder);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/orders/1");

        verify(orderRequestService).createOrder(requestDTO);
        verify(orderMapper).toDTO(savedOrder);
    }

    @Test
    @DisplayName("Should propagate exception when service fails")
    void createOrderError() {
        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        when(orderRequestService.createOrder(any(OrderRequestDTO.class)))
                .thenThrow(new RuntimeException("Error creating order"));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                writeOrderController.createOrder(requestDTO, uriBuilder)
        );
    }
}
