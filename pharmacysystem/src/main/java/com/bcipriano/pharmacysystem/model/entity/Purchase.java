package com.bcipriano.pharmacysystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "purchase", schema = "management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate purchaseDate;

    @Column(name = "note_number")
    private String noteNumber;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "id_supplier")
    private Supplier supplier;

}
