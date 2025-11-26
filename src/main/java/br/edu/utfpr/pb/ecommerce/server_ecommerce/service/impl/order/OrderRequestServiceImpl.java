package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.PostalCodeRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentProductRequest;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.request.ShipmentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.dto.response.ShipmentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.service.MelhorEnvioService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.OrderMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.ShipmentMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.EmbeddedAddress;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrder.IOrderRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.MapperUtils.map;

@Service
public class OrderRequestServiceImpl extends CrudRequestServiceImpl<Order, OrderUpdateDTO, Long> implements IOrderRequestService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final ShipmentMapper shipmentMapper;
    private final MelhorEnvioService melhorEnvioService;

    public OrderRequestServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, AuthService authService, ModelMapper modelMapper, OrderMapper orderMapper, ProductMapper productMapper, ShipmentMapper shipmentMapper, MelhorEnvioService melhorEnvioService) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.shipmentMapper = shipmentMapper;
        this.melhorEnvioService = melhorEnvioService;
    }

    private Order findAndValidateOrder(Long id, User user) {
        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    @Override
    @Transactional
    public Order update(Long id, OrderUpdateDTO updateDTO) {
        User user = authService.getAuthenticatedUser();
        Order order = findAndValidateOrder(id, user);

        if(updateDTO.getAddress() != null) {
            EmbeddedAddress newAddress = map(updateDTO.getAddress(), EmbeddedAddress.class, modelMapper);
            order.setAddress(newAddress);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order createOrder(OrderRequestDTO dto) {
        Map<Long, Integer> quantityByIdMap = dto.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemRequestDTO::getProductId, OrderItemRequestDTO::getQuantity));

// 2. Buscar as entidades no banco
        List<Product> products = productRepository.findAllById(quantityByIdMap.keySet());

// 3. Validação de integridade (Se pediu 5 produtos, tem que voltar 5)
        if (products.size() != quantityByIdMap.size()) {
            throw new ProductNotFoundException("Divergência de produtos: Um ou mais IDs não existem.");
        }

// 4. Construção do Map<Product, Integer> solicitado
        Map<Product, Integer> productQuantityMap = products.stream()
                .collect(Collectors.toMap(
                        product -> product,                                  // Key: A entidade Product
                        product -> quantityByIdMap.get(product.getId())      // Value: A quantidade correspondente
                ));

        if (products.size() != productQuantityMap.size()) {
            throw new ProductNotFoundException("One or more products were not found.");
        }

        // 2. Preparação para API Externa
        ShipmentRequestDTO shipmentRequestDTO = new ShipmentRequestDTO();
        shipmentRequestDTO.setTo(new PostalCodeRequest(dto.getAddress().getCep()));

        // Mapeamento mais seguro passando o Map para garantir a quantidade correta por ID
        List<ShipmentProductRequest> shipmentProductRequests = productMapper.toShipmentProductRequestList(productQuantityMap);
        shipmentRequestDTO.setProducts(shipmentProductRequests);

        // 3. Chamada Externa
        List<ShipmentResponseDTO> shipmentOptions = melhorEnvioService.calculateShipmentByProducts(shipmentRequestDTO);

        // 4. Validação e Seleção do Frete
        ShipmentResponseDTO selectedShipment = shipmentOptions.stream()
                .filter(s -> s.id().equals(dto.getShipmentId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("The selected shipping method is no longer available or is invalid for this order."));

        return saveOrderInTransaction(dto, selectedShipment);
    }

    @Transactional
    protected Order saveOrderInTransaction(OrderRequestDTO dto, ShipmentResponseDTO shipmentData) {
        Order order = orderMapper.toEntity(dto);

        order.setShipment(shipmentMapper.toEmbedded(shipmentData));

        User user = authService.getAuthenticatedUser();
        order.setUser(user);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteAll() {
        User user = authService.getAuthenticatedUser();
        orderRepository.deleteAllByUser(user);
    }

    @Override
    @Transactional
    public void deleteById(Long aLong) {
        User user = authService.getAuthenticatedUser();
        orderRepository.deleteByIdAndUser(aLong, user);
    }

    @Override
    @Transactional
    public void delete(Iterable<? extends Order> iterable) {
        User user = authService.getAuthenticatedUser();
        orderRepository.deleteAllByUserAndIdIn(user, iterable);
    }
}
