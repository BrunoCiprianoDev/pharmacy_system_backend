package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import springfox.documentation.annotations.ApiIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;

    private String cep;

    private String uf;

    private String city;

    private String neightborhood;

    private String addressDetail;

    private String number;

    private String complement;

    public static AddressDTO create(Address address){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(address, AddressDTO.class);
    }

}
