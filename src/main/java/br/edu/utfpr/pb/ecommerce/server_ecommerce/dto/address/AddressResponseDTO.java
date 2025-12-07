package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.address;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.baseDTO.BaseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO extends BaseDTO {
    private Long id;

    private UserResponseDTO user;

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

    private String cep;
}
