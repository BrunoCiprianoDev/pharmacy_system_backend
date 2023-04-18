package com.bcipriano.pharmacysystem.model.entity;

import com.bcipriano.pharmacysystem.model.entity.enums.Position;
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

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "born_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate bornDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_address")
    private Address address;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "secundary_phone")
    private String secundaryPhone;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Position position;
}