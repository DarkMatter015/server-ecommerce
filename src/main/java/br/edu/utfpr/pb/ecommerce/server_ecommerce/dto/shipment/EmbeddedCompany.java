package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EmbeddedCompany(

        @Column(name = "company_name")
        String name,

        @Column(name = "company_picture")
        String picture
) {
}
