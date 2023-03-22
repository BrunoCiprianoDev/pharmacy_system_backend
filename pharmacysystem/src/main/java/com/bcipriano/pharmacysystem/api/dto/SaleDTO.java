package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Long id;

    private LocalDate saleDate;

    private Long employeeId;

    private String employeeName;

    private Long clientId;

    private String clientName;

    public static SaleDTO create(Sale sale){
        ModelMapper modelMapper = new ModelMapper();
        SaleDTO dto = modelMapper.map(sale, SaleDTO.class);
        dto.employeeId = sale.getEmployee().getId();
        dto.employeeName = sale.getEmployee().getName();
        dto.clientId = sale.getClient().getId();
        dto.clientName = sale.getClient().getName();
        return dto;
    }

}
