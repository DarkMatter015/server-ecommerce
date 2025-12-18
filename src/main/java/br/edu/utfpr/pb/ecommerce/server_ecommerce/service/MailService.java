package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.dto.mail.EmailEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.publisher.mail.EmailPublisher;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

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
                        <h2>Hello, %s</h2>
                        <p>You have requested a password change.</p>
                        <p>Click the button below to create a new password:</p>
                        <br>
                        <a href="%s" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                            CHANGE PASSWORD
                        </a>
                        <br><br>
                        <p>Link valid for %d minutes.</p>
                        <p>If you did not request this, ignore this email.</p>
                        <hr>
                        <small>RiffHouse Ecommerce Team</small>
                    </div>
                    """.formatted(user.getDisplayName(), link, expirationTime);

            emailPublisher.send(new EmailEventDTO(user.getEmail(), subject, content));
        } catch (Exception e){
            throw new RuntimeException("Error sending email.", e);
        }
    }
}
