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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Long id;

    private String saleDate;

    private Long employeeId;

    private Employee employee;

    private Long clientId;

    private Client client;

    private List<SaleItem> saleItems;

    public static SaleDTO create(Sale sale){
        ModelMapper modelMapper = new ModelMapper();
        SaleDTO dto = modelMapper.map(sale, SaleDTO.class);
        dto.employeeId = sale.getEmployee().getId();
        if(sale.getClient() != null){
            dto.clientId = sale.getClient().getId();
        }
        return dto;
    }

}
