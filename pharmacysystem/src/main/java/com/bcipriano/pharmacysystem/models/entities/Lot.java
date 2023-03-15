package com.bcipriano.pharmacysystem.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "lot", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "expirationDate")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "id_purchase")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "id_merchandise")
    private Merchandise merchandise;

    @Column(name = "units")
    private Integer units;

}
