package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Embeddable
public record EmbeddedShipmentDTO(

        @NotNull
        @Column(name = "shipment_id")
        Long id,

        @NotNull
        @Column(name = "shipment_name")
        String name,

        @NotNull
        @Column(name = "shipment_price")
        BigDecimal price,

        @Column(name = "shipment_custom_price")
        BigDecimal custom_price,

        @Column(name = "shipment_discount")
        BigDecimal discount,

        @Column(name = "shipment_currency")
        String currency,

        @NotNull
        @Column(name = "shipment_delivery_time")
        Integer delivery_time,

        @NotNull
        @Embedded
        EmbeddedCompanyDTO company
) {
}
