package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.email;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.EmailException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email.EmailEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email.EmailPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailPublisher emailPublisher;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.reset.token.expiration.minutes:15}")
    private int expirationTime;

    private final EmailTemplateProvider emailTemplateProvider;

    public void sendResetPasswordEmail(User user, PasswordResetToken token) {
        try {
            String subject = "Redefinição de Senha - RiffHouse";
            String link = frontendUrl + "/recuperar-senha?token=" + token.getToken();
            String content = emailTemplateProvider.buildPasswordRecoveryEmail(user.getDisplayName(), link, expirationTime);

            emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
        } catch (Exception e) {
            throw new EmailException(ErrorCode.EMAIL_ERROR_SEND, e.getMessage());
        }
    }

    public void sendAlertProductStockAvailableEmail(String email, Product product, Long alertId) throws Exception {
        String subject = "Produto %s Disponível para Compra - RiffHouse".formatted(product.getName());
        String link = frontendUrl + "/produto/" + product.getId();

        String content = emailTemplateProvider.buildStockAlertEmail(email, product.getName(), product.getUrlImage(), product.getQuantityAvailableInStock(), link);

        emailPublisher.send(new EmailEventDTO(email, subject, content, alertId));
    }

    public void sendOrderProcessingEmail(User user, OrderResponseDTO order) throws Exception {
        String subject = "Pedido %s em Processamento - RiffHouse".formatted(order.getId());
        String link = frontendUrl + "/pedidos";
        String content = emailTemplateProvider.buildOrderConfirmationEmail(order, user.getDisplayName(), link);

        emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
    }

    public void sendOrderCancellationEmail(User user, OrderResponseDTO order) throws Exception {
        String subject = "Pedido %s Cancelado - RiffHouse".formatted(order.getId());
        String link = frontendUrl + "/pedidos";
        String content = emailTemplateProvider.buildOrderCancellationEmail(order, user.getDisplayName(), link);

        emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
    }

    public void sendOrderSuccessEmail(User user, OrderResponseDTO order) throws Exception {
        String subject = "Pedido %s Processado com Sucesso - RiffHouse".formatted(order.getId());
        String link = frontendUrl + "/pedidos";
        String content = emailTemplateProvider.buildOrderSuccessEmail(order, user.getDisplayName(), link);

        emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
    }

    public void sendOrderStatusChangeEmail(User user, OrderResponseDTO order) throws Exception {
        String statusLabel = order.getStatus();
        String subject = "Atualização do Pedido %s - RiffHouse".formatted(order.getId());
        String link = frontendUrl + "/pedidos";
        String content = emailTemplateProvider.buildOrderStatusChangeEmail(
                order, user.getDisplayName(), link, statusLabel, resolveStatusColor(statusLabel));

        emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
    }

    public void sendOrderDocumentEmail(User user, Long orderId, String documentName, DocumentType type) throws Exception {
        String subject = "Novo documento no Pedido %s - RiffHouse".formatted(orderId);
        String link = frontendUrl + "/pedidos";
        String content = emailTemplateProvider.buildOrderDocumentEmail(
                user.getDisplayName(), orderId, resolveDocumentLabel(type), documentName, link);

        emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
    }

    private String resolveStatusColor(String status) {
        if (status == null) {
            return "#0056b3";
        }
        return switch (status.toUpperCase()) {
            case "ENTREGUE" -> "#28a745";
            case "CANCELADO" -> "#dc3545";
            case "PROCESSANDO" -> "#f39c12";
            default -> "#0056b3";
        };
    }

    private String resolveDocumentLabel(DocumentType type) {
        if (type == null) {
            return "Documento";
        }
        return switch (type) {
            case NOTA_FISCAL -> "Nota Fiscal";
            case COMPROVANTE -> "Comprovante";
            case OUTRO -> "Documento";
        };
    }
}
