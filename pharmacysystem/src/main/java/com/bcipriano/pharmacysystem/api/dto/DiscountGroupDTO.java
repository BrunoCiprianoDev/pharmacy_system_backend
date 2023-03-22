package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class DiscountGroupDTO {

    private Long id;

    private LocalDate startDate;

    private LocalDate finalDate;

    private Double percentage;

    private Integer minimumUnits;

    public static DiscountGroupDTO create(DiscountGroup discountGroup){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(discountGroup, DiscountGroupDTO.class);
    }

}
