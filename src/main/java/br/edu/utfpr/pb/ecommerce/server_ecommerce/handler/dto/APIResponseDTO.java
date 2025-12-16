package br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseDTO {
    private String message;
    private int status = 200;

    public APIResponseDTO(String message) {
        this.message = message;
    }
}
