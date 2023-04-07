package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDTO {

    private Long id;

    private String registerDate;

    private String description;

    private Long saleItemId;

    private SaleItem saleItem;

    public static ReturnDTO create(Return returnObj){
        ModelMapper modelMapper = new ModelMapper();
        ReturnDTO dto = modelMapper.map(returnObj, ReturnDTO.class);
        dto.saleItemId = returnObj.getSaleItem().getId();
        return dto;
    }
}
