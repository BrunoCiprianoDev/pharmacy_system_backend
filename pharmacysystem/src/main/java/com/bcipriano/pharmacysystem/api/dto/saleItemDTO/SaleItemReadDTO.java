package com.bcipriano.pharmacysystem.api.dto.saleItemDTO;

import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemReadDTO {

    private Long id;

    private Integer units;

    private Double sellPrice;

    private Long saleId;

    private Long lotId;

    private String lotNumber;

    private String merchandiseName;

    private double discountPrice;

    public static SaleItemReadDTO create(SaleItem saleItem){
        ModelMapper modelMapper = new ModelMapper();
        SaleItemReadDTO dto = modelMapper.map(saleItem, SaleItemReadDTO.class);
        dto.saleId = saleItem.getSale().getId();
        dto.lotId = saleItem.getLot().getId();
        dto.lotNumber = saleItem.getLot().getNumber();
        dto.merchandiseName = saleItem.getLot().getMerchandise().getName();
        dto.discountPrice = saleItem.getLot().getMerchandise().getFullPrice();
        return dto;
    }
}