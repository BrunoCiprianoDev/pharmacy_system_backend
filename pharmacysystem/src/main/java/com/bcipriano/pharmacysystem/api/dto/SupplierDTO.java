package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.validation.constraints.Cep;
import com.bcipriano.pharmacysystem.validation.constraints.Cnpj;
import com.bcipriano.pharmacysystem.validation.constraints.Phone;

import com.bcipriano.pharmacysystem.validation.constraints.Uf;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "The supplier name", example = "Medicine Industry", required = true)
    @NotBlank(message = "O campo nome não foi preenchido.")
    private String name;

    @ApiModelProperty(value = "Supplier CNPJ", example = "99.999.9999-99", required = true)
    @Cnpj(message = "Cnpj inválido.")
    private String cnpj;

    @ApiModelProperty(value = "CEP", example = "99.999-999", required = true)
    @Cep(message = "CEP inválido.")
    private String cep;

    @ApiModelProperty(value = "UF", example = "MG", required = true)
    @Uf(message = "Campo UF inválido.")
    private String uf;

    @ApiModelProperty(value = "City", example = "Juiz de Fora", required = true)
    @NotBlank(message = "O campo cidade não foi preenchido.")
    private String city;

    @ApiModelProperty(value = "Neightborhood", example = "Bairro Fabrica", required = true)
    @NotBlank(message = "O campo bairro não foi preechido.")
    private String neightborhood;

    @ApiModelProperty(value = "Address detail", example = "Rua Bernando Mascarenhas", required = true)
    @NotBlank(message = "O campo logradouro não foi preenchido.")
    private String addressDetail;

    @ApiModelProperty(value = "Number", example = "1075", required = true)
    @NotBlank(message = "O campo número não foi preenchido.")
    private String number;

    @ApiModelProperty(value = "Complement", example = "Ap.102", required = true)
    @NotBlank(message = "O campo complemento não foi preenchido.")
    private String complement;

    @ApiModelProperty(value = "Primary phone", example = "(99)9999-9999 or (99)99999-9999", required = true)
    @Phone(message = "Telefone 1 inválido.")
    private String primaryPhone;

    @ApiModelProperty(value = "Secundary phone", example = "(99)9999-9999 or (99)99999-9999", required = true)
    @Phone(message = "Telefone 2 inválido.")
    private String secundaryPhone;

    @ApiModelProperty(value = "Supplier e-mail", example = "industryMD@gmail.com", required = true)
    @Email(message = "E-mail inválido.")
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
