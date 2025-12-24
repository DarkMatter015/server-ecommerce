package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.productStockUpdated;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AlertProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.EmailService;
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
    private final EmailService emailService;

    public void processStockAlerts(ProductStockUpdatedEventDTO event) {
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

        for (AlertProduct alert : pendingAlerts) {
            try {
                emailService.sendAlertProductStockAvailableEmail(alert.getEmail(), event);
                alert.setStatus(AlertProductStatus.SENT);
                alertsToSave.add(alert);
            } catch (Exception e) {
                log.error("Error while sending alert product stock available email to user: {}. Error: {}", productInfo, e.getMessage());
            }
        }
        if (!alertsToSave.isEmpty()) {
            alertRepository.saveAll(alertsToSave);
            log.info("Finished. Updated status for {} alerts.", alertsToSave.size());
        }
        log.info("Finished {} sending of alerts.", numberAlerts);
    }
}
