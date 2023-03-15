package com.bcipriano.pharmacysystem.models.entities;

import com.bcipriano.pharmacysystem.models.enums.Department;
import com.bcipriano.pharmacysystem.models.enums.Stripe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "merchandise", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchandise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "department")
    @Enumerated(value = EnumType.STRING)
    private Department department;

    @Column(name = "classification")
    private String classification;

    @Column(name = "brand")
    private String brand;

    @Column(name = "formule")
    private String formule;

    @Column(name = "stripe")
    @Enumerated(value = EnumType.STRING)
    private Stripe stripe;

    @Column(name = "storageTemperature")
    private BigDecimal storageTemperature;

    @Column(name = "rms")
    private String rms;

    @Column(name = "minimum_stock")
    private Integer minimumStock;

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "maximum_stock")
    private Integer maximumStock;

    @Column(name  = "full_price")
    private BigDecimal fullPrice;

    @Column(name = "comission")
    private BigDecimal comission;

    @Column(name = "pmc")
    private String pmc;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_discount_group")
    private DiscountGroup discountGroup;

}
