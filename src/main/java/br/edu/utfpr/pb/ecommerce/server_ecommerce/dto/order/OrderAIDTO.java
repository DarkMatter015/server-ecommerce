package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAIDTO {
    private Long id;

    private String status;

    private String statusMessage;

    private LocalDateTime data;

    private String address;

    private String payment;

    private String shipment;

    private String trackingCode;

    private BigDecimal total;

    private List<String> items;
}
