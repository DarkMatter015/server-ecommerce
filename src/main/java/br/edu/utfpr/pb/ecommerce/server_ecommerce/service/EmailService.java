package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.EmailException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email.EmailEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email.EmailPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.productStockUpdated.ProductStockUpdatedEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
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

    public void sendResetPasswordEmail(User user, PasswordResetToken token) {
        try {
            String subject = "Redefinição de Senha - RiffHouse";
            String link = frontendUrl + "/recuperar-senha?token=" + token.getToken();

            String content = """
                    <div style="font-family: Arial, sans-serif; color: #333;">
                        <h2>Olá, %s</h2>
                        <p>Você solicitou uma alteração de senha.</p>
                        <p>Clique no botão abaixo para criar uma nova senha:</p>
                        <br>
                        <a href="%s" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                            ALTERAR SENHA
                        </a>
                        <br><br>
                        <p>Link válido por %d minutos.</p>
                        <p>Se você não solicitou isso, ignore este e-mail.</p>
                        <hr>
                        <small>Equipe RiffHouse Ecommerce</small>
                    </div>
                    """.formatted(user.getDisplayName(), link, expirationTime);

            emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
        } catch (Exception e) {
            throw new EmailException(ErrorCode.EMAIL_ERROR_SEND, e.getMessage());
        }
    }

    public void sendAlertProductStockAvailableEmail(String email, ProductStockUpdatedEventDTO product) throws Exception {
        String subject = "Produto %s Disponível para Compra - RiffHouse".formatted(product.productName());
        String link = frontendUrl + "/produto/" + product.productId();

        String content = """
                <div style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Olá, %s</h2>
                    <p>O produto %s que você tinha interesse voltou ao estoque!</p>
                    <p>Clique no botão abaixo para visualizar o produto:</p>
                    <br>
                    <a href="%s" style="background-color: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                        VER PRODUTO
                    </a>
                    <br><br>
                    <p>Quantidade disponível: <strong>%d</strong></p>
                    <p>Se você não solicitou isso, ignore este e-mail.</p>
                    <hr>
                    <small>Equipe RiffHouse Ecommerce</small>
                </div>
                """.formatted(email, product.productName(), link, product.stockQuantity());

        emailPublisher.send(new EmailEventDTO(email, subject, content));
    }
}
