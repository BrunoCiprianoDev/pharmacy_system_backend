package com.bcipriano.pharmacysystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "client", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

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
    private Address address;

    private String phone;

    private String email;

}
