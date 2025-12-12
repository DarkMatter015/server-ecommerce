package br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedAddress {

    @Column(name = "address_street", nullable = false, length = 255)
    private String street;

    @Column(name = "address_number", nullable = false)
    private String number;

    @Column(name = "address_complement")
    private String complement;

    @Column(name = "address_neighborhood")
    private String neighborhood;

    @Column(name = "address_city", nullable = false, length = 255)
    private String city;

    @Column(name = "address_state", nullable = false, length = 255)
    private String state;

    @Column(name = "address_cep", nullable = false, length = 8)
    private String cep;
}
