package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {

    private Long id;

    private String name;

    private String noteNumber;

    private Double total;

    private Long supplierId;

    private String supplierName;

    public static PurchaseDTO create(Purchase purchase){
        ModelMapper modelMapper = new ModelMapper();
        PurchaseDTO dto = modelMapper.map(purchase, PurchaseDTO.class);
        dto.supplierId = purchase.getSupplier().getId();
        dto.supplierName = purchase.getSupplier().getName();
        return dto;
    }

}
