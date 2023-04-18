package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Long id;

    private String saleDate;

    private Long employeeId;

    private String employeeName;

    private Long clientId;

    private String clientName;

    private String clientCpf;

    private List<SaleItemDTO> saleItemsDTO;

    public static SaleDTO create(Sale sale, List<SaleItem> saleItems){
        ModelMapper modelMapper = new ModelMapper();
        SaleDTO dto = modelMapper.map(sale, SaleDTO.class);

        dto.employeeId = sale.getEmployee().getId();
        dto.employeeName = sale.getEmployee().getName();

        if(sale.getClient() != null){
            dto.clientId = sale.getClient().getId();
            dto.clientName = sale.getClient().getName();
            dto.clientCpf = sale.getClient().getCpf();
        }

        List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
        for(SaleItem saleItem : saleItems) {
            saleItemDTOList.add(SaleItemDTO.create(saleItem));
        }
        dto.setSaleItemsDTO(saleItemDTOList);

        return dto;
    }

}