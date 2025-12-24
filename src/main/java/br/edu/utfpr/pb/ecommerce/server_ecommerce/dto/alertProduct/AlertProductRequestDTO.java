package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertProductRequestDTO {
    @NotNull
    @Email
    @Size(min = 4, max = 255)
    private String email;

    @NotNull
    @Positive
    private Long productId;
}
