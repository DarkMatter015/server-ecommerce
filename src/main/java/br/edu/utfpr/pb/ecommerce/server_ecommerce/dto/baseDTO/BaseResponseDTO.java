package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseResponseDTO {

    private Long id;

    private Boolean active;
}
