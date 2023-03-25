package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Profile;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@DataJpaTest
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

    @Test
    public void testSholdReturnAListOfEmployeesByProfile() {

        Employee employeeOne = Employee.builder().profile(Profile.MANAGEMENT).build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().profile(Profile.MANAGEMENT).build();
        employeeRepository.save(employeeTwo);

        Employee employeeThree = Employee.builder().profile(Profile.SELLER).build();
        employeeRepository.save(employeeThree);

        List<Employee>listResponseManagement = employeeRepository.findByProfile(Profile.MANAGEMENT);
        List<Employee>listResponseSeller = employeeRepository.findByProfile(Profile.SELLER);

        Assertions.assertThat(listResponseManagement).hasSize(2);
        Assertions.assertThat(listResponseSeller).hasSize(1);

    }

    @Test
    public void testMustReturnAListWithTwoEmployeesForQueryEmail() {

        Employee employeeOne = Employee.builder().email("email01@email.com").build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().email("otheremail02@email.com").build();
        employeeRepository.save(employeeTwo);

        Employee employeeThree = Employee.builder().email("AnyString").build();
        employeeRepository.save(employeeThree);

        List<Employee> listResponse = employeeRepository.findEmployeesByQuery("email");

        Assertions.assertThat(listResponse).hasSize(2);

    }

    @Test
    public void testMustReturnAListWithTwoEmployeesForQueryName() {

        Employee employeeOne = Employee.builder().name("EmployeeOne").build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().name("EmployeeTwo").build();
        employeeRepository.save(employeeTwo);

        Employee employeeThree = Employee.builder().name("AnyString").build();
        employeeRepository.save(employeeThree);

        List<Employee> listResponse = employeeRepository.findEmployeesByQuery("mplo");

        Assertions.assertThat(listResponse).hasSize(2);

    }

    @Test
    public void testMustReturnAListWithThreeEmployeesForQueryCPF() {

        Employee employeeOne = Employee.builder().cpf("123.000.000").build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().cpf("000.123.000").build();
        employeeRepository.save(employeeTwo);

        Employee employeeThree = Employee.builder().cpf("000.000.123").build();
        employeeRepository.save(employeeThree);

        Employee employeeFour = Employee.builder().cpf("AnyString").build();
        employeeRepository.save(employeeFour);

        List<Employee> listResponse = employeeRepository.findEmployeesByQuery("123");

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testShouldReturnAnEmptyListForADiscrepantQuery(){

        Employee employeeOne = Employee.builder().email("email01@email.com").build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().email("otheremail02@email.com").build();
        employeeRepository.save(employeeTwo);

        List<Employee> listResponse = employeeRepository.findEmployeesByQuery("anyString");

        Assertions.assertThat(listResponse).isEmpty();

    }

    @Test
    public void testMustReturnAListWithThreeEmployeesForQuery() {

        Employee employeeOne = Employee.builder().name("AnyString").build();
        employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().email("AnyString").build();
        employeeRepository.save(employeeTwo);

        Employee employeeThree = Employee.builder().primaryPhone("AnyString").build();
        employeeRepository.save(employeeThree);

        Employee employeeFour = Employee.builder().cpf("AnyString").build();
        employeeRepository.save(employeeFour);

        Employee employeeFive = Employee.builder().secundaryPhone("AnyString").build();
        employeeRepository.save(employeeFive);

        List<Employee> listResponse = employeeRepository.findEmployeesByQuery("ing");

        Assertions.assertThat(listResponse).hasSize(5);

    }

}
