package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusChangeDTO {

    @NotNull
    private Long statusId;

    private String statusMessage;
}
