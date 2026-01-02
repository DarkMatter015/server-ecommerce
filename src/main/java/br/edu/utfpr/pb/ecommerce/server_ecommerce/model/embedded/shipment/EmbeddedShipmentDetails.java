package br.edu.utfpr.pb.ecommerce.server_ecommerce.model.embedded.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "shipment_id")
    private Long id;

    @NotNull
    @Column(name = "shipment_name")
    private String name;

    @NotNull
    @Column(name = "shipment_price")
    private BigDecimal price;

    @Column(name = "shipment_custom_price")
    private BigDecimal custom_price;

    @Column(name = "shipment_discount")
    private BigDecimal discount;

    @Column(name = "shipment_currency")
    private String currency;

    @NotNull
    @Column(name = "shipment_delivery_time")
    private Integer delivery_time;

    @NotNull
    @Embedded
    private EmbeddedCompanyDetails company;
}