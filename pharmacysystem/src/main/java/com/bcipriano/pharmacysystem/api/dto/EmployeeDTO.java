package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

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

    private String primaryPhone;

    private String secundaryPhone;

    private String email;

    private String position;

    private String positionValue;

    public static EmployeeDTO create(Employee employee) {

        ModelMapper modelMapper = new ModelMapper();
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        employeeDTO.bornDate = employee.getBornDate().format(formatter);

        employeeDTO.cep = employee.getAddress().getCep();
        employeeDTO.uf = employee.getAddress().getUf();
        employeeDTO.city = employee.getAddress().getCity();
        employeeDTO.neightborhood = employee.getAddress().getNeightborhood();
        employeeDTO.addressDetail = employee.getAddress().getAddressDetail();
        employeeDTO.number = employee.getAddress().getNumber();
        employeeDTO.complement = employee.getAddress().getComplement();

        employeeDTO.positionValue = employee.getPosition().getValue();

        return employeeDTO;
    }

}