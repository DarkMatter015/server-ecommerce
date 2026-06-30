package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.email;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.FormatUtils.CURRENCY_FORMAT;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.FormatUtils.DATE_FORMAT;

@Component
@RequiredArgsConstructor
public class EmailTemplateProvider {

    private final SpringTemplateEngine templateEngine;

    public String buildOrderSuccessEmail(OrderResponseDTO order, String userName, String link) {
        Context context = baseOrderContext(order, userName, link, true);
        context.setVariable("paymentMethod", resolvePaymentMethod(order));
        context.setVariable("shipmentMethod", resolveShipmentMethod(order));
        return templateEngine.process("email/order-success", context);
    }

    public String buildOrderConfirmationEmail(OrderResponseDTO order, String userName, String link) {
        Context context = baseOrderContext(order, userName, link, true);
        return templateEngine.process("email/order-confirmation", context);
    }

    public String buildOrderCancellationEmail(OrderResponseDTO order, String userName, String link) {
        Context context = baseOrderContext(order, userName, link, false);
        context.setVariable("statusMessage", order.getStatusMessage());
        return templateEngine.process("email/order-cancellation", context);
    }

    public String buildOrderStatusChangeEmail(OrderResponseDTO order, String userName, String link,
                                              String statusLabel, String headerColor) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("orderId", order.getId());
        context.setVariable("date", formatDate(order));
        context.setVariable("statusLabel", statusLabel);
        context.setVariable("statusMessage", order.getStatusMessage());
        context.setVariable("headerColor", headerColor);
        context.setVariable("link", link);
        return templateEngine.process("email/order-status-change", context);
    }

    public String buildOrderDocumentEmail(String userName, Long orderId, String documentLabel,
                                          String documentName, String link) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("orderId", orderId);
        context.setVariable("documentLabel", documentLabel);
        context.setVariable("documentName", documentName);
        context.setVariable("link", link);
        return templateEngine.process("email/order-document", context);
    }

    public String buildStockAlertEmail(String userName, String productName, String productImageUrl,
                                       int stockQuantity, String link) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("productName", productName);
        context.setVariable("productImageUrl", productImageUrl);
        context.setVariable("stockQuantity", stockQuantity);
        context.setVariable("stockColor", stockQuantity < 5 ? "#dc3545" : "#28a745");
        context.setVariable("scarcityText", stockQuantity < 5 ? "Corra! Restam apenas" : "Quantidade disponível:");
        context.setVariable("link", link);
        return templateEngine.process("email/stock-alert", context);
    }

    public String buildPasswordRecoveryEmail(String userName, String link, long expirationMinutes) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("link", link);
        context.setVariable("expirationMinutes", expirationMinutes);
        return templateEngine.process("email/password-recovery", context);
    }

    // --- MÉTODOS PRIVADOS DE SUPORTE ---

    private Context baseOrderContext(OrderResponseDTO order, String userName, String link, boolean showAddress) {
        OrderTotals totals = calculateTotals(order);

        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("link", link);
        context.setVariable("orderId", order.getId());
        context.setVariable("date", formatDate(order));
        context.setVariable("items", buildItems(order.getOrderItems()));
        context.setVariable("subtotal", totals.subTotal());
        context.setVariable("shipping", totals.shipping());
        context.setVariable("total", totals.grandTotal());
        context.setVariable("showAddress", showAddress);
        context.setVariable("address", buildAddress(order));
        return context;
    }

    private List<Map<String, Object>> buildItems(List<OrderItemResponseDTO> items) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (OrderItemResponseDTO item : items) {
            Map<String, Object> view = new LinkedHashMap<>();
            view.put("imageUrl", item.getProduct().getUrlImage());
            view.put("name", item.getProduct().getName());
            view.put("quantity", item.getQuantity());
            view.put("price", CURRENCY_FORMAT.format(item.getTotalPrice()));
            result.add(view);
        }
        return result;
    }

    private String buildAddress(OrderResponseDTO order) {
        if (order.getAddress() == null) {
            return "";
        }
        return String.format("%s, %s - CEP: %s",
                order.getAddress().getStreet() != null ? order.getAddress().getStreet() : "Rua",
                order.getAddress().getNumber(),
                order.getAddress().getCep());
    }

    private OrderTotals calculateTotals(OrderResponseDTO order) {
        BigDecimal totalItems = order.getOrderItems().stream()
                .map(OrderItemResponseDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingCost = (order.getShipment() != null && order.getShipment().getPrice() != null)
                ? order.getShipment().getPrice()
                : BigDecimal.ZERO;

        return new OrderTotals(
                CURRENCY_FORMAT.format(totalItems),
                CURRENCY_FORMAT.format(shippingCost),
                CURRENCY_FORMAT.format(totalItems.add(shippingCost))
        );
    }

    private String formatDate(OrderResponseDTO order) {
        return order.getData().format(DATE_FORMAT);
    }

    private String resolvePaymentMethod(OrderResponseDTO order) {
        return (order.getPayment() != null && order.getPayment().getName() != null) ? order.getPayment().getName() : "Processado";
    }

    private String resolveShipmentMethod(OrderResponseDTO order) {
        return (order.getShipment() != null && order.getShipment().getName() != null) ? order.getShipment().getName() : "Calculando informações da entrega";
    }

    private record OrderTotals(String subTotal, String shipping, String grandTotal) {
    }
}
