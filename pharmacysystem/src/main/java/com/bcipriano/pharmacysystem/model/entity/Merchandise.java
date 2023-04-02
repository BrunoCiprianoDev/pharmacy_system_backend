package com.bcipriano.pharmacysystem.model.entity;

import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
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
    private Long id;

    private String name;

    private String code;

    @Enumerated(value = EnumType.STRING)
    private Department department;

    private String classification;

    private String brand;

    private String formule;

    @Enumerated(value = EnumType.STRING)
    private Stripe stripe;

    @Column(name = "storage_temperature")
    private Float storageTemperature;

    private String rms;

    @Column(name = "minimum_stock")
    private Integer minimumStock;

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "maximum_stock")
    private Integer maximumStock;

    @Column(name  = "full_price")
    private Double fullPrice;

    private Double comission;

    private Double pmc;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_discount_group")
    private DiscountGroup discountGroup;

}
