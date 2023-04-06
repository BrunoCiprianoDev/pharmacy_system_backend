package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.model.entity.Address;
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

    private String cep;

    private String cnpj;

    private String uf;

    private String city;

    private String neightborhood;

    private String addressDetail;

    private String number;

    private String complement;

    private String primaryPhone;

    private String secundaryPhone;

    private String email;

    public static SupplierDTO create(Supplier supplier){
        ModelMapper modelMapper = new ModelMapper();
        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        supplierDTO.cep = supplier.getAddress().getCep();
        supplierDTO.uf = supplier.getAddress().getUf();
        supplierDTO.city = supplier.getAddress().getCity();
        supplierDTO.neightborhood = supplier.getAddress().getNeightborhood();
        supplierDTO.addressDetail = supplier.getAddress().getAddressDetail();
        supplierDTO.number = supplier.getAddress().getNumber();
        supplierDTO.complement = supplier.getAddress().getComplement();

        return supplierDTO;
    }


}
