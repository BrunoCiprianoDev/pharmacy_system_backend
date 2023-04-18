package com.bcipriano.pharmacysystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "loss", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate registerDate;

    private Integer units;

    @ManyToOne
    @JoinColumn(name = "id_lot")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;

    private String description;

}