package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class EmailConsumer {

    private final JavaMailSender javaMailSender;

    private final ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.sender.name:RiffHouse Ecommerce}")
    private String senderName;

    @RabbitListener(queues = "${email.queue.name}")
    public void consumeEmailMessage(String message) throws JsonProcessingException {
        log.info("Consuming EMAIl message: {}", message);
        var emailEventDTO = objectMapper.readValue(message, EmailEventDTO.class);
        try {
            log.info("Processing send email: {}", emailEventDTO.email());

            MimeMessage emailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail, senderName);
            helper.setTo(emailEventDTO.email());
            helper.setSubject(emailEventDTO.subject());
            helper.setText(emailEventDTO.htmlContent(), true);

            javaMailSender.send(emailMessage);

            log.info("Email successfully sent to: {}", emailEventDTO.email());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", emailEventDTO.email(), e.getMessage(), e);        }
    }
}
