package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    public final Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void testFindEmployeeByQuery() {

        Employee employee1 = Employee.builder()
                .name("Employee1")
                .cpf("111.1111.1111.11")
                .bornDate(LocalDate.parse("2000-04-14"))
                .primaryPhone("(11)1111-1111")
                .secundaryPhone("(12)1111-1111")
                .email("emailEmployee1")
                .build();

        Employee employee2 = Employee.builder()
                .name("Employee2")
                .cpf("222.222.222.22")
                .bornDate(LocalDate.parse("2000-04-15"))
                .primaryPhone("(22)2222-2222")
                .secundaryPhone("(21)2222-2222")
                .email("emailEmployee2")
                .build();

        Employee employee1Saved = employeeRepository.save(employee1);
        Employee employee2Saved = employeeRepository.save(employee2);

        //Get Employee by name
        Page<Employee> result1 = employeeRepository.findEmployeesByQuery("Employee", pageable);
        Assertions.assertThat(result1.getContent()).hasSize(2);

        //Get Employee by email
        Page<Employee> result2 = employeeRepository.findEmployeesByQuery("email", pageable);
        Assertions.assertThat(result2.getContent()).hasSize(2);

        //Get Employee1 by cpf
        Page<Employee> result3 = employeeRepository.findEmployeesByQuery("111.1111.1111.11", pageable);
        Assertions.assertThat(result2.getContent().get(0)).isEqualTo(employee1Saved);

        //Get Employee2 by phone1
        Page<Employee> result4 = employeeRepository.findEmployeesByQuery("(22)2222-2222", pageable);
        Assertions.assertThat(result4.getContent().get(0)).isEqualTo(employee2Saved);

    }

    @Test
    public void testFindEmployeeByPosition() {

        Employee employee1 = Employee.builder().position(Position.MANAGER).build();
        Employee employee2 = Employee.builder().position(Position.MANAGER).build();
        Employee employee3 = Employee.builder().position(Position.MANAGER).build();

        Employee employee4 = Employee.builder().position(Position.SELLER).build();
        Employee employee5 = Employee.builder().position(Position.SELLER).build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        employeeRepository.save(employee4);
        employeeRepository.save(employee5);

        Page<Employee> result1 = employeeRepository.findByPosition(Position.MANAGER, pageable);
        Assertions.assertThat(result1.getContent()).hasSize(3);

        Page<Employee> result2 = employeeRepository.findByPosition(Position.SELLER, pageable);
        Assertions.assertThat(result2.getContent()).hasSize(2);

    }

    @Test
    public void testFindEmployeeById() {

        Employee employee1 = Employee.builder()
                .name("Employee1")
                .cpf("111.1111.1111.11")
                .bornDate(LocalDate.parse("2000-04-14"))
                .primaryPhone("(11)1111-1111")
                .secundaryPhone("(12)1111-1111")
                .email("emailEmployee1")
                .build();

        Employee employee1Saved = employeeRepository.save(employee1);

        Optional<Employee> result = employeeRepository.findById(employee1Saved.getId());
        Assertions.assertThat(result.get()).isEqualTo(employee1Saved);

    }

    @Test
    public void testExistsByCpf() {

        Employee employee = Employee.builder().cpf("000.000.000-00").build();
        Employee employeeSaved = employeeRepository.save(employee);

        boolean result = employeeRepository.existsByCpf(employee.getCpf());
        Assertions.assertThat(result).isTrue();

    }

}
