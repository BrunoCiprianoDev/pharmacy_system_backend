package com.bcipriano.pharmacysystem.model.entity;

import javax.persistence.*;
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

    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_address")
    private Address address;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "secundary_phone")
    private String secundaryPhone;

    private String email;

}