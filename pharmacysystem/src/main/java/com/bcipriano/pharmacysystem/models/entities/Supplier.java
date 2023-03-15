package com.bcipriano.pharmacysystem.models.entities;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "uf")
    private String uf;

    @Column(name = "city")
    private String city;

    @Column(name = "neightborhood")
    private String neightborhood;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "primaryPhone")
    private String primaryPhone;

    @Column(name = "secundaryPhone")
    private String secondaryPhone;

    @Column(name = "email")
    private String email;

}
