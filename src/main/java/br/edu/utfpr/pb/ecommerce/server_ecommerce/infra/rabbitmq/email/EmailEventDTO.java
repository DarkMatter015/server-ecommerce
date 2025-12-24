package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email;

import lombok.Builder;

@Builder
public record EmailEventDTO(
        String email,
        String subject,
        String htmlContent
) {
}
