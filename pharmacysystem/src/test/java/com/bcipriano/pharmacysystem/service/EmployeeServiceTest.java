package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class EmployeeServiceTest {

    @SpyBean
    EmployeeServiceImpl employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    public void testEmployeeValidate() {

        Employee employee = Employee.builder().build();

        // Exception case name is NULL
        Throwable exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome é obrigatório.");
        employee.setName("AnyString");

        // Exception case cpf is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CPF é obrigatório.");
        employee.setCpf("");

        // Exception case cpf not matches with pattern
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("CPF inválido.");
        employee.setCpf("000.000.000-00");

        // Exception case born date is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo data de nascimento é obrigatório.");

        // Exception case age max > 120
        employee.setBornDate(LocalDate.of(1900, 01, 01));
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Data de nascimento inválida.");

        // Exception case age min < 18
        employee.setBornDate(LocalDate.now());
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Data de nascimento inválida.");
        employee.setBornDate(LocalDate.of(2000,1,1));

        // Exception if address is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Endereço inválido.");

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
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone 1 é obrigatório.");
        employee.setPrimaryPhone("");

        // Exception if primaryPhone not matches with pattern
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 1 inválido.");
        employee.setPrimaryPhone("(00)00000-0000");

        // Exception if secundaryPhone not matches with pattern
        employee.setSecundaryPhone("");
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 2 inválido.");
        employee.setSecundaryPhone("(00)0000-0000");

        // Exception if e-mail is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo e-mail é obrigatório.");
        employee.setEmail("");

        // Exception if e-mail not matches pattern
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("E-mail inválido.");
        employee.setEmail("email@email.com");

        // Exception if password is NULL
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("A senha não foi preenchida.");
        employee.setPassword("");

        // Exception if password.length() < 6
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("A senha de conter, no mínimo, 6 caracteres.");

        //Exception if the password does not meet the requirements
        employee.setPassword("123456");
        exception = Assertions.catchThrowable(() -> employeeService.validateEmployee(employee));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("A senha deve conter pelo menos 2 caracteres especiais e 1 letra maiúscula.");
        employee.setPassword("P@assw0rd#");

    }

}
