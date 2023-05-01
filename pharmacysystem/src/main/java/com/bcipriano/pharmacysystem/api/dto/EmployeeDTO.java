package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Employee;
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
public class EmployeeDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "The employee's name", example = "José Itamar", required = true)
    @NotBlank(message = "O campo nome não foi preenchido.")
    private String name;

    @ApiModelProperty(value = "Employee CPF", example = "999.999.999-99", required = true)
    @Cpf(message = "CPF inválido.")
    private String cpf;

    @ApiModelProperty(value = "Employee birth date", example = "2000-01-02 (YYYY-MM-DD)", required = true)
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

    @ApiModelProperty(value = "Primary phone", example = "(99)9999-9999 or (99)99999-9999", required = true)
    @Phone(message = "Telefone 1 inválido.")
    private String primaryPhone;

    @ApiModelProperty(value = "Secundary phone", example = "(99)9999-9999 or (99)99999-9999", required = true)
    @Phone(message = "Telefone 2 inválido.")
    private String secundaryPhone;

    @ApiModelProperty(value = "Employee e-mail", example = "jose@gmail.com", required = true)
    @Email(message = "E-mail inválido.")
    private String email;

    @ApiModelProperty(value = "Employee position", example = "(GERENTE, VENDEDOR)", required = true)
    @PositionValid(message = "Função inválida.")
    private String position;

    public static EmployeeDTO create(Employee employee) {

        ModelMapper modelMapper = new ModelMapper();
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        employeeDTO.birthDate = employee.getBirthDate().format(formatter);

        employeeDTO.cep = employee.getAddress().getCep();
        employeeDTO.uf = employee.getAddress().getUf();
        employeeDTO.city = employee.getAddress().getCity();
        employeeDTO.neightborhood = employee.getAddress().getNeightborhood();
        employeeDTO.addressDetail = employee.getAddress().getAddressDetail();
        employeeDTO.number = employee.getAddress().getNumber();
        employeeDTO.complement = employee.getAddress().getComplement();

        return employeeDTO;
    }

}