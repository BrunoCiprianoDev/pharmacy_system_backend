package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.AuthErrorException;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class EmployeeServiceImplTest {

    @SpyBean
    EmployeeServiceImpl employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    public void testMustSuccessfullyAuthenticateTheUser() {

        String email = "email@email.com";
        String password = "password";

        Employee employee = Employee.builder().email(email).password(password).id(1L).build();

        Mockito.when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        Employee result = employeeService.authentication(email, password);

        Assertions.assertThat(result).isNotNull();

    }

    @Test
    public void testReturnErrorForInvalidEmail() {

        Mockito.when(employeeRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> employeeService.authentication("email@email.com", "password"));

        Assertions.assertThat(exception).isInstanceOf(AuthErrorException.class).hasMessage("Funcionário não encontrado!");

    }

    @Test
    public void testReturnErrorForInvalidPassword() {

        String email = "email@email.com";
        String password = "password";

        Employee employee = Employee.builder().email(email).password(password).id(1L).build();

        Mockito.when(employeeRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(employee));

        Throwable exception = Assertions.catchThrowable(() -> employeeService.authentication(email, "anyString"));

        Assertions.assertThat(exception).hasMessage("Senha inválida!");

    }

    @Test
    public void testMustSuccesfullySaveUser() {

        Mockito.doNothing().when(employeeService).validateEmployeeByEmail(Mockito.anyString());

        Mockito.doNothing().when(employeeService).validatePassword(Mockito.anyString());

        String email = "email@email.com";
        String password = "password";

        Employee employee = Employee.builder().email(email).password(password).id(1L).build();

        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        Employee employeeSaved = employeeService.saveEmployee(Employee.builder().email(email).password(password).build());

        Assertions.assertThat(employeeSaved).isEqualTo(employee);

    }

    @Test
    public void testReturnErrorWhenSavedEmployeenContendInvalidEmail() {

        Mockito.when(employeeRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> employeeService.saveEmployee(Employee.builder().email(Mockito.anyString()).build()));

        Assertions.assertThat(exception).hasMessage("Já existe um funcionario cadastrado com esse e-mail.");

    }

    @Test
    public void testReturnExceptionWhenInsertInvalidPassword() {

        Employee employee = Employee.builder().email("AnyString").password(null).build();

        Mockito.doNothing().when(employeeService).validateEmployeeByEmail(Mockito.anyString());

        Throwable exception = Assertions.catchThrowable(() -> employeeService.saveEmployee(employee));
        Assertions.assertThat(exception).hasMessage("Senha inválida!");

        employee.setPassword("12345");
        exception = Assertions.catchThrowable(() -> employeeService.saveEmployee(employee));
        Assertions.assertThat(exception).hasMessage("A senha deve ter, no mínimo 6 characteres");

        employee.setPassword("123456");
        exception = Assertions.catchThrowable(() -> employeeService.saveEmployee(employee));
        Assertions.assertThat(exception).hasMessage("Insira pelo menos 2 caracteres especiais");

        employee.setPassword("@123456#");
        exception = Assertions.catchThrowable(() -> employeeService.saveEmployee(employee));
        Assertions.assertThat(exception).hasMessage("A senha deve conter, pelo menos 1 caracter com letra maiúscula");

    }


    @Test
    public void testReturnEmployeeById() {

        Employee employee = Employee.builder().id(1L).build();

        Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(employee.getId());

        Assertions.assertThat(result.get()).isEqualTo(employee);

    }

    @Test
    public void testReturnExceptionByInvalidEmployeeId() {

        Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> employeeService.getEmployeeById(Mockito.anyLong()));

        Assertions.assertThat(exception).hasMessage("Id inválido!");

    }


}
