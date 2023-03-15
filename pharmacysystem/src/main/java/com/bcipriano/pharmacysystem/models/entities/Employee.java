package com.bcipriano.pharmacysystem.models.entities;

import com.bcipriano.pharmacysystem.models.enums.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "employee", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "purchase_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate bornDate;

    @Column(name = "cep")
    private String cep;

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
    private String secundaryPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "profile")
    @Enumerated(value = EnumType.STRING)
    private Profile profile;
}
