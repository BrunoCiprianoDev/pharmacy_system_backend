package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import lombok.AllArgsConstructor;
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

    private String department;

    private String departmentValue;

    private String classification;

    private String brand;

    private String formule;

    private String stripe;

    private String stripeValue;

    private String storageTemperature;

    private String storageTemperatureValue;

    private String rms;

    private Integer minimumStock;

    private Integer maximumStock;

    private Double fullPrice;

    private Double comission;

    private Double pmc;

    private String description;

    private Long discountGroupId;

    private DiscountGroup discountGroup;


    public static MerchandiseDTO create(Merchandise merchandise){

        ModelMapper modelMapper = new ModelMapper();
        MerchandiseDTO merchandiseDTO = modelMapper.map(merchandise, MerchandiseDTO.class);

        if(merchandise.getDiscountGroup() != null){
            merchandiseDTO.discountGroupId = merchandise.getDiscountGroup().getId();
        }

        merchandiseDTO.storageTemperatureValue = merchandise.getStorageTemperature().getValue();
        merchandiseDTO.stripeValue = merchandise.getStripe().getValue();
        merchandiseDTO.departmentValue = merchandise.getDepartment().getValue();

        return merchandiseDTO;
    }

}