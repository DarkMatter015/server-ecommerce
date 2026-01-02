package br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmbeddedShipmentDetails {

    @Column(name = "shipment_id")
    private Long id;

    @Column(name = "shipment_name")
    private String name;

    @Column(name = "shipment_price")
    private BigDecimal price;

    @Column(name = "shipment_custom_price")
    private BigDecimal custom_price;

    @Column(name = "shipment_discount")
    private BigDecimal discount;

    @Column(name = "shipment_currency")
    private String currency;

    @Column(name = "shipment_delivery_time")
    private Integer delivery_time;

    @Embedded
    private EmbeddedCompanyDetails company;
}