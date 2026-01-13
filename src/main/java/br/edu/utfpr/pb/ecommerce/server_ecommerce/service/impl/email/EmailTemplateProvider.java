package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.email;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.FormatUtils.CURRENCY_FORMAT;
import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.FormatUtils.DATE_FORMAT;

@Component
public class EmailTemplateProvider {

    public String buildOrderSuccessEmail(OrderResponseDTO order, String userName, String link) {
        OrderTotals totals = calculateTotals(order);
        String itemsHtml = buildItemsHtml(order.getOrderItems());

        return """
                <!DOCTYPE html>
                <html>
                %s <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f4f4; padding: 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                    %s <tr>
                                        <td style="padding: 30px 30px 10px 30px;">
                                            <h2 style="color: #333; margin-top: 0;">Olá, %s!</h2>
                                            <p style="color: #666; font-size: 16px; line-height: 1.5;">Temos ótimas notícias! Seu pedido foi processado com sucesso e logo será enviado.</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 0 30px;">
                                            <table width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f9f9f9; border-radius: 5px; margin-bottom: 20px;">
                                                <tr>
                                                    <td valign="top" width="50%%" style="padding: 15px;">
                                                        <p style="margin: 0; color: #888; font-size: 11px; text-transform: uppercase; font-weight: bold;">Forma de Pagamento</p>
                                                        <p style="margin: 5px 0 0 0; color: #333; font-size: 14px; font-weight: bold;">%s</p>
                                                    </td>
                                                    <td valign="top" width="50%%" style="padding: 15px; border-left: 1px solid #eee;">
                                                        <p style="margin: 0; color: #888; font-size: 11px; text-transform: uppercase; font-weight: bold;">Envio via</p>
                                                        <p style="margin: 5px 0 0 0; color: #333; font-size: 14px; font-weight: bold;">%s</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    %s %s %s </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                getCommonHead(),
                getHeaderHtml("#28a745", "PAGAMENTO APROVADO"),
                userName,
                resolvePaymentMethod(order),
                resolveShipmentMethod(order),
                buildOrderBodyHtml(order, itemsHtml, totals, true),
                getButtonHtml(link, "Acompanhar Entrega"),
                getFooterHtml()
        );
    }

    public String buildOrderCancellationEmail(OrderResponseDTO order, String userName, String link) {
        OrderTotals totals = calculateTotals(order);
        String itemsHtml = buildItemsHtml(order.getOrderItems());

        return """
                <!DOCTYPE html>
                <html>
                %s
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f4f4; padding: 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                    %s <tr>
                                        <td style="padding: 30px 30px 10px 30px;">
                                            <h2 style="color: #333; margin-top: 0;">Olá, %s.</h2>
                                            <p style="color: #666; font-size: 16px; line-height: 1.5;">Houve um problema ao processar seu pedido e ele precisou ser cancelado.</p>
                                            <table width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #fff5f5; border-left: 4px solid #dc3545; margin: 20px 0;">
                                                <tr>
                                                    <td style="padding: 15px;">
                                                        <p style="margin: 0; color: #dc3545; font-size: 12px; font-weight: bold; text-transform: uppercase;">Motivo do Cancelamento</p>
                                                        <p style="margin: 5px 0 0 0; color: #333; font-size: 14px;">%s</p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <p style="color: #666; font-size: 14px;">Caso tenha sido uma cobrança no cartão, o estorno será realizado automaticamente.</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 0 30px;">
                                            <h3 style="color: #777; border-bottom: 2px solid #eee; padding-bottom: 10px; font-size: 16px;">
                                                Resumo do Pedido #%s <span style="font-size: 14px; font-weight: normal; color: #999; float: right;">%s</span>
                                            </h3>
                                            <table width="100%%" cellspacing="0" cellpadding="0" border="0">
                                                %s <tr>
                                                    <td colspan="2" align="right" style="padding-top: 15px; color: #777; font-size: 14px;">Total Cancelado:</td>
                                                    <td align="right" style="padding-top: 15px; color: #dc3545; font-weight: bold; font-size: 16px;">%s</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    %s %s </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                getCommonHead(),
                getHeaderHtml("#dc3545", "PEDIDO CANCELADO"),
                userName,
                order.getStatusMessage(),
                order.getId(),
                formatDate(order),
                itemsHtml,
                totals.grandTotal,
                getButtonHtml(link, "Ver Detalhes do Pedido"),
                getFooterHtml()
        );
    }

    public String buildOrderConfirmationEmail(OrderResponseDTO order, String userName, String link) {
        OrderTotals totals = calculateTotals(order);
        String itemsHtml = buildItemsHtml(order.getOrderItems());

        return """
                <!DOCTYPE html>
                <html>
                %s
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f4f4; padding: 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                    %s <tr>
                                        <td style="padding: 30px 30px 10px 30px;">
                                            <h2 style="color: #333; margin-top: 0;">Olá, %s!</h2>
                                            <p style="color: #666; font-size: 16px; line-height: 1.5;">Seu pedido foi recebido com sucesso e já está sendo processado.</p>
                                            <table width="100%%" style="background-color: #f9f9f9; border-radius: 5px; margin-top: 15px;">
                                                <tr>
                                                    <td style="padding: 15px;">
                                                        <p style="margin: 0; color: #888; font-size: 12px;">NÚMERO DO PEDIDO</p>
                                                        <p style="margin: 5px 0 0 0; color: #333; font-weight: bold;">#%s</p>
                                                    </td>
                                                    <td style="padding: 15px;">
                                                        <p style="margin: 0; color: #888; font-size: 12px;">DATA DO PEDIDO</p>
                                                        <p style="margin: 5px 0 0 0; color: #333; font-weight: bold;">%s</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    %s %s %s </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                getCommonHead(),
                getHeaderHtml("#1a1a1a", "PEDIDO RECEBIDO"),
                userName,
                order.getId(),
                formatDate(order),
                buildOrderBodyHtml(order, itemsHtml, totals, true),
                getButtonHtml(link, "Acompanhar Pedido"),
                getFooterHtml()
        );
    }

    public String buildStockAlertEmail(String userName, String productName, String productImageUrl, int stockQuantity, String link) {
        String stockColor = stockQuantity < 5 ? "#dc3545" : "#28a745";
        String scarcityText = stockQuantity < 5 ? "Corra! Restam apenas" : "Quantidade disponível:";

        return """
                <!DOCTYPE html>
                <html>
                %s <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f4f4; padding: 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                
                                    %s <tr>
                                        <td style="padding: 30px 30px 10px 30px; text-align: center;">
                                            <h2 style="color: #333; margin-top: 0;">A espera acabou, %s!</h2>
                                            <p style="color: #666; font-size: 16px; line-height: 1.5;">
                                                Você pediu e nós avisamos. O produto que você estava de olho voltou ao nosso estoque.
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td align="center" style="padding: 10px 30px;">
                                            <div style="border: 1px solid #eee; border-radius: 8px; padding: 20px; max-width: 300px;">
                                                <img src="%s" alt="%s" width="200" height="auto" style="display: block; margin: 0 auto; border-radius: 4px;">
                
                                                <h3 style="color: #333; font-size: 18px; margin: 15px 0 5px 0;">%s</h3>
                
                                                <p style="margin: 10px 0 0 0; font-size: 14px; color: #666;">
                                                    %s <strong style="color: %s; font-size: 16px;">%d unidades</strong>
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                
                                    %s <tr>
                                        <td style="padding: 0 30px 20px 30px; text-align: center;">
                                            <p style="color: #999; font-size: 12px; margin: 0;">
                                                Não perca tempo, os produtos podem acabar rapidamente.
                                            </p>
                                        </td>
                                    </tr>
                
                                    %s </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                getCommonHead(),
                getHeaderHtml("#1a1a1a", "ESTOQUE RENOVADO"), // Header Preto ou Verde (#28a745) ficam bons
                userName,
                productImageUrl, // URL da imagem
                productName,     // Alt text
                productName,     // Nome do produto visível
                scarcityText,    // Texto "Restam apenas" ou "Disponível"
                stockColor,      // Cor do número (Vermelho ou Verde)
                stockQuantity,   // Número
                getButtonHtml(link, "GARANTIR O MEU AGORA"),
                getFooterHtml()
        );
    }

    public String buildPasswordRecoveryEmail(String userName, String link, long expirationMinutes) {
        return """
                <!DOCTYPE html>
                <html>
                %s <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f4f4; padding: 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                    <tr>
                                        <td style="background-color: #0056b3; padding: 20px; text-align: center;">
                                            <h1 style="color: #ffffff; margin: 0; font-size: 24px;">RiffHouse</h1>
                                            <p style="color: #ffffff; margin: 5px 0 0 0; font-weight: bold; font-size: 14px; opacity: 0.9;">REDEFINIÇÃO DE SENHA</p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding: 30px 30px 20px 30px;">
                                            <h2 style="color: #333; margin-top: 0;">Olá, %s.</h2>
                                            <p style="color: #666; font-size: 16px; line-height: 1.5;">
                                                Recebemos uma solicitação para redefinir a senha da sua conta na RiffHouse.
                                            </p>
                                            <p style="color: #666; font-size: 16px; line-height: 1.5;">
                                                Para continuar e criar uma nova senha, clique no botão abaixo:
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td align="center" style="padding: 10px 30px 30px 30px;">
                                            <a href="%s" style="background-color: #007bff; color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 5px; font-weight: bold; font-size: 16px; display: inline-block;">
                                                CRIAR NOVA SENHA
                                            </a>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding: 0 30px;">
                                            <table width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #eef6fc; border-radius: 5px; border: 1px solid #d6e9f9;">
                                                <tr>
                                                    <td style="padding: 15px;">
                                                        <p style="margin: 0; color: #004085; font-size: 13px;">
                                                            <strong>⏳ Atenção:</strong> Este link é válido por apenas <strong>%d minutos</strong> por motivos de segurança.
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding: 30px 30px 10px 30px;">
                                            <p style="color: #999; font-size: 14px; margin: 0; line-height: 1.4;">
                                                Se você não solicitou essa alteração, não se preocupe. Sua senha atual permanecerá ativa e você pode ignorar este e-mail.
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding: 20px 30px; border-top: 1px solid #eee;">
                                            <p style="color: #888; font-size: 12px; margin-bottom: 5px;">
                                                Está com problemas no botão? Copie e cole o link abaixo no seu navegador:
                                            </p>
                                            <p style="margin: 0;">
                                                <a href="%s" style="color: #007bff; font-size: 11px; word-break: break-all;">%s</a>
                                            </p>
                                        </td>
                                    </tr>
                
                                    %s </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                getCommonHead(),
                userName,
                link,
                expirationMinutes,
                link, link,
                getFooterHtml()
        );
    }

    // --- MÉTODOS PRIVADOS DE SUPORTE ---

    private String buildItemsHtml(List<OrderItemResponseDTO> items) {
        StringBuilder sb = new StringBuilder();
        for (OrderItemResponseDTO item : items) {
            sb.append("""
                        <tr>
                            <td width="20%%" style="padding: 10px; border-bottom: 1px solid #eee;">
                                <img src="%s" alt="Produto" width="60" height="auto" style="display: block; border-radius: 4px;">
                            </td>
                            <td width="50%%" style="padding: 10px; border-bottom: 1px solid #eee; font-family: Arial, sans-serif; font-size: 14px; color: #333;">
                                <strong>%s</strong><br>
                                <span style="font-size: 12px; color: #777;">Qtd: %d</span>
                            </td>
                            <td width="30%%" align="right" style="padding: 10px; border-bottom: 1px solid #eee; font-family: Arial, sans-serif; font-size: 14px; color: #333;">
                                %s
                            </td>
                        </tr>
                    """.formatted(
                    item.getProduct().getUrlImage(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    CURRENCY_FORMAT.format(item.getTotalPrice())
            ));
        }
        return sb.toString();
    }

    private String buildOrderBodyHtml(OrderResponseDTO order, String itemsHtml, OrderTotals totals, boolean showAddress) {
        String addressBlock = "";
        if (showAddress) {
            String fullAddress = String.format("%s, %s - CEP: %s",
                    order.getAddress().getStreet() != null ? order.getAddress().getStreet() : "Rua",
                    order.getAddress().getNumber(),
                    order.getAddress().getCep());

            addressBlock = """
                        <tr>
                            <td style="padding: 20px 30px;">
                                <table width="100%%" cellspacing="0" cellpadding="0" border="0">
                                    <tr>
                                        <td valign="top" width="50%%" style="padding-right: 15px;">
                                            <h4 style="color: #333; margin-bottom: 10px; font-size: 14px;">Endereço de Entrega:</h4>
                                            <p style="color: #666; font-size: 13px; line-height: 1.4; margin: 0;">%s</p>
                                        </td>
                                        <td valign="top" width="50%%">
                                            <table width="100%%" cellspacing="0" cellpadding="5">
                                                <tr><td style="color: #666; font-size: 13px;">Subtotal:</td><td align="right" style="color: #333; font-size: 13px;">%s</td></tr>
                                                <tr><td style="color: #666; font-size: 13px;">Frete:</td><td align="right" style="color: #333; font-size: 13px;">%s</td></tr>
                                                <tr><td style="color: #333; font-weight: bold; font-size: 15px; border-top: 1px solid #ccc; padding-top: 10px;">Total:</td>
                                                    <td align="right" style="color: #28a745; font-weight: bold; font-size: 15px; border-top: 1px solid #ccc; padding-top: 10px;">%s</td></tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    """.formatted(fullAddress, totals.subTotal, totals.shipping, totals.grandTotal);
        }

        return """
                    <tr>
                        <td style="padding: 0 30px;">
                            <h3 style="color: #333; border-bottom: 2px solid #eee; padding-bottom: 10px; font-size: 16px;">
                                Resumo do Pedido #%s <span style="font-size: 14px; font-weight: normal; color: #999; float: right;">%s</span>
                            </h3>
                            <table width="100%%" cellspacing="0" cellpadding="0" border="0">%s</table>
                        </td>
                    </tr>
                    %s
                """.formatted(order.getId(), formatDate(order), itemsHtml, addressBlock);
    }

    // --- MÉTODOS DE COMPONENTES HTML REUTILIZÁVEIS ---

    private String getCommonHead() {
        return "<head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>";
    }

    private String getHeaderHtml(String bgColor, String title) {
        return """
                    <tr>
                        <td style="background-color: %s; padding: 20px; text-align: center;">
                            <h1 style="color: #ffffff; margin: 0; font-size: 24px;">RiffHouse</h1>
                            <p style="color: #ffffff; margin: 5px 0 0 0; font-weight: bold; font-size: 14px; opacity: 0.9;">%s</p>
                        </td>
                    </tr>
                """.formatted(bgColor, title);
    }

    private String getButtonHtml(String link, String text) {
        return """
                    <tr>
                        <td align="center" style="padding: 30px;">
                            <a href="%s" style="background-color: #28a745; color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 5px; font-weight: bold; font-size: 16px; display: inline-block;">%s</a>
                        </td>
                    </tr>
                """.formatted(link, text);
    }

    private String getFooterHtml() {
        return """
                    <tr>
                        <td style="background-color: #f4f4f4; padding: 20px; text-align: center; color: #888; font-size: 12px;">
                            <p style="margin: 0;">RiffHouse Ecommerce</p>
                            <p style="margin: 5px 0;">Obrigado por comprar conosco!</p>
                        </td>
                    </tr>
                """;
    }

    // --- UTILITÁRIOS ---

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
