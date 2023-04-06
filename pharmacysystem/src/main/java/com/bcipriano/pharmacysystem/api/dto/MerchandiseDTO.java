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

    private String department;

    private String classification;

    private String brand;

    private String formule;

    private Stripe stripe;

    private Float storageTemperature;

    private String rms;

    private Integer minimumStock;

    private Integer currentStock;

    private Integer maximumStock;

    private Double fullPrice;

    private Double comission;

    private Double pmc;

    private String description;

    private DiscountGroup discountGroup;

    private Long idDiscountGroup;

    public static MerchandiseDTO create(Merchandise merchandise){
        ModelMapper modelMapper = new ModelMapper();
        MerchandiseDTO merchandiseDTO = modelMapper.map(merchandise, MerchandiseDTO.class);
        if(merchandise.getDiscountGroup() != null){
            merchandiseDTO.idDiscountGroup = merchandise.getDiscountGroup().getId();
        }
        return merchandiseDTO;
    }


}
