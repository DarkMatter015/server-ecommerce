package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class EmailConsumer {

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${mail.sender.name:RiffHouse Ecommerce}")
    private String senderName;
    @Value("${email.wait.attempts}")
    private int emailWaitAttempts;

    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;
    private final IAlertProductRequestService alertProductRequestService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${email.queue.name}")
    public void consumeEmailMessage(EmailEventDTO emailEventDTO) throws MessagingException, UnsupportedEncodingException {
        log.info("Consuming EMAIl message: {}", emailEventDTO);
        if (emailEventDTO.alertId() != null) log.info("[MAIN] Trying to send email ALERT_ID: {}", emailEventDTO.alertId());

        log.info("Processing send email: {}", emailEventDTO.email());

        MimeMessage emailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, StandardCharsets.UTF_8.name());

        helper.setFrom(fromEmail, senderName);
        helper.setTo(emailEventDTO.email());
        helper.setSubject(emailEventDTO.subject());
        helper.setText(emailEventDTO.htmlContent(), true);

        javaMailSender.send(emailMessage);

        log.info("Email successfully sent to: {}", emailEventDTO.email());
        if (emailEventDTO.alertId() != null) alertProductRequestService.updateAlertStatus(emailEventDTO.alertId(), AlertProductStatus.SENT);
    }

    @RabbitListener(queues = "${email.dlq.name}")
    public void processEmailDlq(Message message) {
        // 1. Recuperar contagem atual de retries do cabeçalho
        Integer retriesHeader = (Integer) message.getMessageProperties().getHeaders().get("x-dlq-retry-count");
        int currentRetries = retriesHeader == null ? 0 : retriesHeader;

        // Convertemos o body para DTO apenas para pegar o ID ou Logar (opcional neste ponto se for reenviar)
        EmailEventDTO emailEvent;
        try {
            emailEvent = objectMapper.readValue(message.getBody(), EmailEventDTO.class);
        } catch (IOException e) {
            log.error("Fatal deserialization error. Discarding.", e);
            return; // Ack e lixo
        }
        log.info("[DLQ] Processing fail ALERT_ID: {}. Attempt extended: {}/3", emailEvent.alertId(), currentRetries);


        if (currentRetries < emailWaitAttempts) {
            // --- CENÁRIO: TENTAR NOVAMENTE MAIS TARDE ---
            log.warn("Email failed. Sending to WaitQueue for retrieval. {}/{}. AlertId: {}", currentRetries + 1, emailWaitAttempts, emailEvent.alertId());

            int nextRetryCount = currentRetries + 1;
            message.getMessageProperties().getHeaders().put("x-dlq-retry-count", nextRetryCount);

            // Reenvia para a WAIT QUEUE com o cabeçalho atualizado
            rabbitTemplate.send("email.wait.queue", message);

            // O método termina sem erro -> ACK na DLQ (sai da DLQ, vai para WaitQueue)

        } else {
            // --- CENÁRIO: DESISTÊNCIA TOTAL (FAILED) ---
            log.error("Extended attempt limit exceeded (3x). Alert marked as FAILED.. AlertId: {}", emailEvent.alertId());

            if (emailEvent.alertId() != null) {
                alertProductRequestService.updateAlertStatus(emailEvent.alertId(), AlertProductStatus.FAILED);
            }

            // O método termina -> ACK na DLQ (mensagem morre aqui)
        }
    }
}