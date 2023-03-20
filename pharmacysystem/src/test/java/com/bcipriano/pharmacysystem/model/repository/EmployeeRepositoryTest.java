package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void checkForAnEmployeeByEmail() {

        Employee employee = Employee.builder().email("email@email.com").build();

        employeeRepository.save(employee);

        boolean result = employeeRepository.existsByEmail("email@email.com");

        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void testCheckNoneexistentEmail() {

        boolean result = employeeRepository.existsByEmail("anyString");

        Assertions.assertThat(result).isFalse();

    }

    @Test
    public void testReturnsAEmployeerByEmail() {

        Employee employee = Employee.builder().email("email@email.com").build();

        Employee employeeResponse = employeeRepository.save(employee);

        Assertions.assertThat(employeeResponse.getId()).isNotNull();

    }

    @Test
    public void testReturnsNullForNonexistentEmail() {

        Employee employee = Employee.builder().email("email@email.com").build();

        employeeRepository.save(employee);

        Optional<Employee> result = employeeRepository.findByEmail("anyString");

        Assertions.assertThat(result.isPresent()).isFalse();

    }

 /*   @Test
    public void testEmployeesWhenSearchingForAnEmail() {

        Employee employeeOne = Employee.builder().email("email01@email.com").build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().email("otheremail02@email.com").build();
        employeeRepository.save(employeeTwo);

        List<Employee> listResponse = employeeRepository.findEmployeesByQuery("email");

        Assertions.assertThat(listResponse).hasSize(2);

    }*/


}
