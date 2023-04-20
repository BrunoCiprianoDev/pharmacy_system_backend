package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Long id;

    private String name;

    private String cpf;

    private String bornDate;

    private String cep;

    private String uf;

    private String city;

    private String neightborhood;

    private String addressDetail;

    private String number;

    private String complement;

    private String phone;

    private String email;

    public static ClientDTO create(Client client) {

        ModelMapper modelMapper = new ModelMapper();
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        clientDTO.bornDate = client.getBornDate().format(formatter);


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
