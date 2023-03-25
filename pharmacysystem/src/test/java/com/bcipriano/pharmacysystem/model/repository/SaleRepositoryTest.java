package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class SaleRepositoryTest {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    EmployeeRepository employeeRepository;


    @Autowired
    ClientRepository clientRepository;

    @Test
    public void testMustReturnListOfSalesByEmployeeId() {

        Employee employee = Employee.builder().build();
        Employee employeeSaved  = employeeRepository.save(employee);

        Sale saleOne = Sale.builder().employee(employee).build();
        saleRepository.save(saleOne);

        Sale saleTwo = Sale.builder().employee(employee).build();
        saleRepository.save(saleTwo);

        Sale saleThree = Sale.builder().employee(employee).build();
        saleRepository.save(saleThree);

        List<Sale> listResponse = saleRepository.findByEmployeeId(employeeSaved.getId());

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfSalesByClientId(){

        Client client = Client.builder().build();
        Client clientSaved = clientRepository.save(client);

        Sale saleOne = Sale.builder().client(client).build();
        saleRepository.save(saleOne);

        Sale saleTwo = Sale.builder().client(client).build();
        saleRepository.save(saleTwo);

        Sale saleThree = Sale.builder().client(client).build();
        saleRepository.save(saleThree);

        List<Sale> listResponse = saleRepository.findByClientId(clientSaved.getId());

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfSalesBySaleDate(){

        Sale saleOne = Sale.builder().saleDate(LocalDate.of(2023,01,01)).build();
        saleRepository.save(saleOne);

        Sale saleTwo = Sale.builder().saleDate(LocalDate.of(2023, 01,01)).build();
        saleRepository.save(saleTwo);

        Sale saleThree = Sale.builder().saleDate(LocalDate.of(2023,01,01)).build();
        saleRepository.save(saleThree);

        List<Sale> listResponse = saleRepository.findBySaleDate(LocalDate.of(2023,01,01));

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfSalesByQuery(){

        Employee employee = Employee.builder().name("EmployeeName").build();
        Employee employeeSaved = employeeRepository.save(employee);

        Client client = Client.builder().name("ClientName").build();
        Client clientSaved = clientRepository.save(client);

        Sale saleOne = Sale.builder().employee(employeeSaved).client(clientSaved).build();
        saleRepository.save(saleOne);

        Sale saleTwo = Sale.builder().employee(employeeSaved).build();
        saleRepository.save(saleTwo);

        List<Sale> listResponse = saleRepository.findSaleByQuery("Name");

        Assertions.assertThat(listResponse).hasSize(2);

    }






}
