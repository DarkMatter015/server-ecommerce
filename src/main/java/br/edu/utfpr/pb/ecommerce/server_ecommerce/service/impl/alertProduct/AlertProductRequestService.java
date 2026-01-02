package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.mapper.AlertProductMapper;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AlertProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.product.ProductResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.alertProduct.IValidateAlertProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService.isAuthenticated;

@Service
@Slf4j
public class AlertProductRequestService extends BaseRequestServiceImpl<AlertProduct, AlertProductRequestDTO, Long> implements IAlertProductRequestService {

    private final AuthService authService;
    private final ProductResponseServiceImpl productResponseService;
    private final AlertProductRepository alertProductRepository;
    private final IAlertProductResponseService alertProductResponseService;
    private final AlertProductMapper alertProductMapper;
    private final List<IValidateAlertProduct> validateAlertProducts;

    public AlertProductRequestService(AlertProductRepository repository,
                                      AlertProductResponseService crudResponseService,
                                      AuthService authService,
                                      ProductResponseServiceImpl productResponseService,
                                      IAlertProductResponseService alertProductResponseService,
                                      AlertProductMapper alertProductMapper,
                                      List<IValidateAlertProduct> validateAlertProducts) {
        super(repository, crudResponseService);
        this.authService = authService;
        this.productResponseService = productResponseService;
        this.alertProductRepository = repository;
        this.alertProductResponseService = alertProductResponseService;
        this.alertProductMapper = alertProductMapper;
        this.validateAlertProducts = validateAlertProducts;
    }


    @Override
    @Transactional
    public AlertProductResponseDTO createAlert(AlertProductRequestDTO request) {
        Product product = productResponseService.findById(request.getProductId());
        User user = isAuthenticated() ? authService.getAuthenticatedUser() : null;
        String targetEmail = user != null ? user.getEmail() : request.getEmail();

        boolean exists = alertProductRepository.existsByEmailAndProductAndStatus(
                targetEmail,
                product,
                AlertProductStatus.PENDING
        );

        if (exists)
            throw new BusinessException(ErrorCode.ALERT_PRODUCT_PENDING_EXISTS);

        AlertProduct alert = super.save(alertProductMapper.toEntity(request, product, user));

        return alertProductMapper.toDTO(alert);
    }

    @Override
    @Transactional
    public AlertProductResponseDTO cancelAlert(Long id) {
        AlertProduct alertProduct = alertProductResponseService.findById(id);
        log.info("Canceling alert ID: {}", id);
        alertProduct.setStatus(AlertProductStatus.CANCELLED);
        alertProduct = super.save(alertProduct);
        return alertProductMapper.toDTO(alertProduct);
    }

    @Override
    @Transactional
    public AlertProductResponseDTO activateAlert(Long id) {
        AlertProduct alertProduct = alertProductResponseService.findById(id);
        if (AlertProductStatus.PENDING.equals(alertProduct.getStatus())) {
            log.info("Alert ID {} is already PENDING. Skipping activation.", id);
            return alertProductMapper.toDTO(alertProduct);
        }

        for (IValidateAlertProduct validator : validateAlertProducts) {
            validator.validate(alertProduct);
        }

        log.info("Reactivating alert ID: {}", id);
        alertProduct.setStatus(AlertProductStatus.PENDING);
        AlertProduct savedAlert = super.save(alertProduct);
        return alertProductMapper.toDTO(savedAlert);
    }

    @Override
    @Transactional
    public void syncOrphanAlerts(User user) {
        log.info("SYNC orphan alerts to User email: {}", user.getEmail());
        alertProductRepository.linkOrphanAlertsToUser(user.getEmail(), user);
    }

    @Override
    @Transactional
    public void updateAlertStatus(Long alertId, AlertProductStatus status) {
        log.info("Updating Alert Status to '{}'.", status);
        alertProductRepository.updateAlertStatus(alertId, status);
    }
}
