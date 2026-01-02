package br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmbeddedCompanyDetails {

        @Column(name = "company_name")
        private String name;

        @Column(name = "company_picture")
        private String picture;
}