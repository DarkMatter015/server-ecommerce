package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.*;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.FilterManager;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class SoftDeleteController {

    private final Map<String, BaseRepository<?, Long>> repositoryMap = new HashMap<>();
    private final FilterManager filterManager;

    // Injeção de todos os repositórios
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final RoleRepository roleRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderItemsRepository orderItemsRepository;

    public SoftDeleteController(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, OrderRepository orderRepository, AddressRepository addressRepository, PaymentRepository paymentRepository, RoleRepository roleRepository, OrderStatusRepository orderStatusRepository, OrderItemsRepository orderItemsRepository, FilterManager filterManager) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.paymentRepository = paymentRepository;
        this.roleRepository = roleRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.filterManager = filterManager;
    }

    @PostConstruct
    public void initRepositoryMap() {
        repositoryMap.put("products", productRepository);
        repositoryMap.put("categories", categoryRepository);
        repositoryMap.put("users", userRepository);
        repositoryMap.put("orders", orderRepository);
        repositoryMap.put("addresses", addressRepository);
        repositoryMap.put("payments", paymentRepository);
        repositoryMap.put("roles", roleRepository);
        repositoryMap.put("order-status", orderStatusRepository);
        repositoryMap.put("order-items", orderItemsRepository);
    }

    private BaseRepository<?, Long> getRepositoryForEntity(String entityName) {
        BaseRepository<?, Long> repo = repositoryMap.get(entityName.toLowerCase());
        if (repo == null) {
            throw new IllegalArgumentException("Entidade desconhecida: " + entityName);
        }
        return repo;
    }

    @GetMapping("/{entityName}/inactive")
    @Transactional(readOnly = true)
    public ResponseEntity<List<?>> getInactiveEntities(@PathVariable String entityName) {
        BaseRepository<?, Long> repository = getRepositoryForEntity(entityName);
        filterManager.enableActiveFilter(false);
        List<?> inactiveEntities = repository.findAll();
        filterManager.disableActiveFilter();
        return ResponseEntity.ok(inactiveEntities);
    }

    @Transactional
    @PatchMapping("/{entityName}/{id}/activate")
    public ResponseEntity<Void> activateEntity(@PathVariable String entityName, @PathVariable Long id) {
        BaseRepository<?, Long> repository = getRepositoryForEntity(entityName);
        filterManager.disableActiveFilter();
        repository.activate(id);
        return ResponseEntity.ok().build();
    }
}
