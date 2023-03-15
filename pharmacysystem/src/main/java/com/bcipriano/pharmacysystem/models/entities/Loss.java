package com.bcipriano.pharmacysystem.models.entities;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "register_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate registerDate;

    @Column(name = "units")
    private Integer units;

    @ManyToOne
    @JoinColumn(name = "id_lot")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @Column(name = "description")
    private String description;

}
