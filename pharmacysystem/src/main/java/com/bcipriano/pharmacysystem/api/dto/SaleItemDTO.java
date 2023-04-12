package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.Sale;
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

    private Sale sale;

    private Long lotId;

    private Lot lot;

    public static SaleItemDTO create(SaleItem saleItem){
        ModelMapper modelMapper = new ModelMapper();
        SaleItemDTO dto = modelMapper.map(saleItem, SaleItemDTO.class);
        dto.saleId = saleItem.getSale().getId();
        dto.lotId = saleItem.getLot().getId();
        return dto;
    }
}
