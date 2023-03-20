package com.bcipriano.pharmacysystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String cnpj;

    private String uf;

    private String city;

    private String neightborhood;

    private String address;

    private String number;

    private String complement;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "secundary_phone")
    private String secondaryPhone;

    private String email;

}
