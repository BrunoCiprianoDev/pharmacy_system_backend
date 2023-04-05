package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountGroupDTO {

    private Long id;

    private String name;

    private String startDate;

    private String finalDate;

    private Double percentage;

    private Integer minimumUnits;

    private String description;

    public static DiscountGroupDTO create(DiscountGroup discountGroup) {
        ModelMapper modelMapper = new ModelMapper();
        DiscountGroupDTO discountGroupDTO = modelMapper.map(discountGroup, DiscountGroupDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        discountGroupDTO.startDate = discountGroup.getStartDate().format(formatter);
        discountGroupDTO.finalDate = discountGroup.getFinalDate().format(formatter);

        return discountGroupDTO;
    }

}
