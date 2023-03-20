package com.bcipriano.pharmacysystem.model.entity;

import com.bcipriano.pharmacysystem.model.entity.enums.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

    private String name;

    private String cpf;

    @Column(name = "born_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate bornDate;

    private String cep;

    private String city;

    private String neightborhood;

    private String address;

    private String number;

    private String complement;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "secundary_phone")
    private String secundaryPhone;

    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Profile profile;
}
