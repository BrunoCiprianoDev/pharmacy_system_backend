package com.bcipriano.pharmacysystem.api.dto.saleDTO;

import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemReadDTO;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleReadDTO {

    private Long id;

    private String saleDate;

    private Long employeeId;

    private String employeeName;

    private Long clientId;

    private String clientName;

    private String clientCpf;

    private double total;

    private List<SaleItemReadDTO> saleItems;

    public static SaleReadDTO create(Sale sale, List<SaleItem> saleItems){
        ModelMapper modelMapper = new ModelMapper();
        SaleReadDTO dto = modelMapper.map(sale, SaleReadDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        dto.saleDate = sale.getSaleDate().format(formatter);

        dto.employeeId = sale.getEmployee().getId();
        dto.employeeName = sale.getEmployee().getName();

        if(sale.getClient() != null){
            dto.clientId = sale.getClient().getId();
            dto.clientName = sale.getClient().getName();
            dto.clientCpf = sale.getClient().getCpf();
        } else {
            dto.clientName = "Não informado.";
            dto.clientCpf = " ";

        }

        List<SaleItemReadDTO> saleItemReadDTOList = new ArrayList<>();
        double total = 0d;
        for(SaleItem saleItem : saleItems) {
            saleItemReadDTOList.add(SaleItemReadDTO.create(saleItem));
            total += saleItem.getUnits() * saleItem.getSellPrice();
        }
        dto.total = total;
        dto.setSaleItems(saleItemReadDTOList);

        return dto;
    }

}