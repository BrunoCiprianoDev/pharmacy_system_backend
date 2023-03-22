package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {

    private Long id;

    private String name;

    private String uf;

    private String city;

    private String neightborhood;

    private String address;

    private String number;

    private String complement;

    private String primaryPhone;

    private String secundaryPhone;

    private String email;

    public static SupplierDTO create(Supplier supplier){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(supplier, SupplierDTO.class);
    }


}