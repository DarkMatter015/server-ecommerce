package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.ResourceNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AlertProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockAlertOrchestrator {

    private final AlertProductRepository alertRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public void processStockAlerts(AlertProductUpdatedEventDTO event) {
        String productInfo = event.productId() + " - " + event.productName();

        log.info("Starting process of alert stock product: {}", productInfo);

        List<AlertProduct> pendingAlerts = alertRepository.findAllByProduct_IdAndStatus(
                event.productId(),
                AlertProductStatus.PENDING
        );

        if (pendingAlerts.isEmpty()) {
            log.info("No pending alerts found for product: {}", productInfo);
            return;
        }
        int numberAlerts = pendingAlerts.size();

        log.info("Found {} users waiting this product.", numberAlerts);

        List<AlertProduct> alertsToSave = new ArrayList<>();

        Product product = productRepository.findById(event.productId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        for (AlertProduct alert : pendingAlerts) {
            try {
                emailService.sendAlertProductStockAvailableEmail(alert.getEmail(), product, alert.getId());
                alert.setStatus(AlertProductStatus.PROCESSING);
                alertsToSave.add(alert);
            } catch (Exception e) {
                log.error("Error while sending alert product stock available email to user: {}. Error: {}", alert.getEmail(), e.getMessage());
            }
        }
        if (!alertsToSave.isEmpty()) {
            alertRepository.saveAll(alertsToSave);
            log.info("Finished. Updated status for {} alerts.", alertsToSave.size());
        }
        log.info("Finished {} sending of alerts.", numberAlerts);
    }
}
