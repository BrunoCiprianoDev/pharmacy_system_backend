package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemDTO {

    private Long id;

    private Integer units;

    private Double sellPrice;

    private Long saleId;

    private Long lotId;

    private String lotNumber;

    public static SaleItemDTO create(SaleItem saleItem){
        ModelMapper modelMapper = new ModelMapper();
        SaleItemDTO dto = modelMapper.map(saleItem, SaleItemDTO.class);
        dto.saleId = saleItem.getSale().getId();
        dto.lotId = saleItem.getLot().getId();
        dto.lotNumber = saleItem.getLot().getNumber();
        return dto;
    }
}
