package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.validation.constraints.*;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "The client name", example = "Maria Torres", required = true)
    @NotBlank(message = "O campo nome não foi preenchido.")
    private String name;

    @ApiModelProperty(value = "Client CPF", example = "999.999.999-99", required = true)
    @Cpf(message = "CPF inválido.")
    private String cpf;

    @ApiModelProperty(value = "Client birth date", example = "1985-01-02 (YYYY-MM-DD)", required = true)
    @BirthDate(message = "Data de nascimento inválida.")
    private String birthDate;

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

    @ApiModelProperty(value = "Phone", example = "(99)9999-9999 or (99)99999-9999", required = true)
    @Phone(message = "Telefone inválido.")
    private String phone;

    @ApiModelProperty(value = "Client e-mail", example = "jose@gmail.com", required = true)
    @Email(message = "E-mail inválido.")
    private String email;

    public static ClientDTO create(Client client) {

        ModelMapper modelMapper = new ModelMapper();
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        clientDTO.cep = client.getAddress().getCep();
        clientDTO.uf = client.getAddress().getUf();
        clientDTO.city = client.getAddress().getCity();
        clientDTO.neightborhood = client.getAddress().getNeightborhood();
        clientDTO.addressDetail = client.getAddress().getAddressDetail();
        clientDTO.number = client.getAddress().getNumber();
        clientDTO.complement = client.getAddress().getComplement();

        return clientDTO;
    }

}
