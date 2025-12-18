package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderItem;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderItemNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.OrderNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.ProductNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Order;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.OrderItem;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderItemsRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.OrderRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IOrderItems.IOrderItemsRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateQuantityOfProduct;

@Service
public class OrderItemsRequestServiceImpl extends CrudRequestServiceImpl<OrderItem, OrderItemUpdateDTO, Long> implements IOrderItemsRequestService {

    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;

    public OrderItemsRequestServiceImpl(OrderItemsRepository orderItemsRepository, OrderItemsResponseServiceImpl orderItemsResponseService, ProductRepository productRepository, OrderRepository orderRepository, AuthService authService) {
        super(orderItemsRepository, orderItemsResponseService);
        this.orderItemsRepository = orderItemsRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.authService = authService;
    }

    private OrderItem findAndValidateOrderItem(Long id, User user) {
        return orderItemsRepository.findByIdAndOrder_User_Id(id, user.getId())
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found or you don't have permission to access it."));
    }


    @Override
    @Transactional
    public OrderItem update(Long id, OrderItemUpdateDTO updateDTO) {
        User user = authService.getAuthenticatedUser();
        OrderItem item = findAndValidateOrderItem(id, user);

        if (updateDTO.getQuantity() != null) {

            if (updateDTO.getQuantity() > item.getQuantity()) {
                Integer decreaseQuantity = updateDTO.getQuantity() - item.getQuantity();

                validateQuantityOfProduct(decreaseQuantity, item.getProduct());
                item.setQuantity(updateDTO.getQuantity());
                item.getProduct().decreaseQuantity(decreaseQuantity);
            }
            else if (updateDTO.getQuantity() < item.getQuantity()) {
                item.getProduct().increaseQuantity(item.getQuantity() - updateDTO.getQuantity());
                item.setQuantity(updateDTO.getQuantity());
            }
        }


        return orderItemsRepository.save(item);
    }

    @Override
    @Transactional
    public OrderItem createOrderItem(OrderItemRequestDTO dto) {
        User user = authService.getAuthenticatedUser();
        Order order = orderRepository.findByIdAndUser(dto.getOrderId(), user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);

        validateQuantityOfProduct(dto.getQuantity(), product);
        item.setQuantity(dto.getQuantity());
        product.decreaseQuantity(dto.getQuantity());

        order.addItem(item);

        return orderItemsRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = authService.getAuthenticatedUser();
        OrderItem item = findAndValidateOrderItem(id, user);

        orderItemsRepository.delete(item);
    }
}
