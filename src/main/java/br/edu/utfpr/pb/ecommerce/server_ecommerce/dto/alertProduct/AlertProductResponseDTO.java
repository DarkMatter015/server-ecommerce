package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertProductResponseDTO implements Identifiable<Long> {
    private Long id;
    private String email;
    private UserResponseDTO user;
    private Long productId;
    private String productName;
    private String requestDate;
    private String processedAt;
    private String status;
}
