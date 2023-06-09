package com.bcipriano.pharmacysystem.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Entity
@Table(name = "sale_item", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer units;

    @Column(name = "sell_price")
    private Double sellPrice;

    @ManyToOne
    @JoinColumn(name = "id_sale")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "id_lot")
    private Lot lot;

    @Transient
    private double totalItem;

    @PostLoad
    public void inicializaCampoCalculado() {
        double result = this.sellPrice * this.units;
    }

}