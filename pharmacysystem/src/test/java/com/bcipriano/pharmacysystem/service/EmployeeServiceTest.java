package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class EmployeeServiceTest {

    @SpyBean
    EmployeeService employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    public void validateEmployeeTest() {

        Employee employee = Employee.builder().build();

        // Exception case name is NULL
        Throwable exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome do funcionário é obrigatório.");
        employee.setName("AnyString");

        // Exception case cpf is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CPF do funcionário é obrigatório.");
        employee.setCpf("");

        // Exception case cpf not matches with pattern
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("CPF do funcionário inválido.");
        employee.setCpf("000.000.000-00");

        // Exception case born date is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo data de nascimento do funcionário é obrigatório.");

        // Exception case age max > 120
        employee.setBornDate(LocalDate.of(1900, 01, 01));
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Data de nascimento do funcionário inválida.");

        // Exception case age min < 18
        employee.setBornDate(LocalDate.now());
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Data de nascimento do funcionário inválida.");
        employee.setBornDate(LocalDate.of(2000,1,1));

        // Exception if address is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Endereço do funcionário inválido.");

        employee.setAddress(Address.builder()
                .cep("00.000-000")
                .uf("XX")
                .city("AnyString")
                .neightborhood("AnyString")
                .addressDetail("AnyString")
                .number("AnyString")
                .complement("AnyString")
                .build());

        // Exception if primaryPhone is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone 1 do funcionário é obrigatório.");
        employee.setPrimaryPhone("");

        // Exception if primaryPhone not matches with pattern
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 1 do funcionário é inválido.");
        employee.setPrimaryPhone("(00)00000-0000");

        // Exception if secundaryPhone not matches with pattern
        employee.setSecundaryPhone("");
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 2 do funcionário é inválido.");
        employee.setSecundaryPhone("(00)0000-0000");

        // Exception if e-mail is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo e-mail do funcionário é obrigatório.");
        employee.setEmail("");

        // Exception if e-mail not matches pattern
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("E-mail do funcionário é inválido.");
        employee.setEmail("email@email.com");

    }

    @Test
    public void saveEmployeeExceptionTest() {

        Mockito.when(employeeRepository.existsByCpf(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> employeeService.saveEmployee(Employee.builder()
                .address(Address.builder().build())
                .build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void updateEmployeeExceptionTest() {

        Mockito.when(employeeRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> employeeService.updateEmployee(Employee.builder()
                .address(Address.builder().build())
                .build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void getEmployeeByIdExceptionTest() {

        Mockito.when(employeeRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> employeeService.getEmployeeById(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(NotFoundException.class);

    }

    @Test
    public void deleteEmployeeByIdExceptionTest() {

        Mockito.when(employeeRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> employeeService.deleteEmployee(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(NotFoundException.class);

        Mockito.verify(employeeRepository, Mockito.never()).deleteById(Mockito.any());

    }


}
