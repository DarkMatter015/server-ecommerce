package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.sender.name:RiffHouse Ecommerce}")
    private String senderName;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.reset.token.expiration.minutes:15}")
    private int expirationTime;

    @Async
    public void sendResetPasswordEmail(User user, PasswordResetToken token) {
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

        sendHtmlEmail(user.getEmail(), subject, content);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail, senderName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

            log.info("Reset email successfully sent to: {}", to);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
}
