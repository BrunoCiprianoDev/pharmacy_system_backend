package com.bcipriano.pharmacysystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "discount_group", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name ="start_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate startDate;

    @Column(name ="final_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate finalDate;

    private Double percentage;

    @Column(name = "minimum_units")
    private Integer minimumUnits;

}
