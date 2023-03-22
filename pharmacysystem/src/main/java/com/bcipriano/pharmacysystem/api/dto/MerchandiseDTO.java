package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchandiseDTO {

    private Long id;

    private String name;

    private String code;

    private Department department;

    private String classification;

    private String formule;

    private Stripe stripe;

    private Float storageTemperature;

    private String rms;

    private Integer minimumStock;

    private Integer currentStock;

    private Integer maximumStock;

    private Double fullPrice;

    private Double comission;

    private String pmc;

    private String description;

    private DiscountGroup discountGroup;

    public static MerchandiseDTO create(Merchandise merchandise){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(merchandise, MerchandiseDTO.class);
    }


}
