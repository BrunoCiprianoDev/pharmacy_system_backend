package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class SaleRepositoryTest {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    SaleItemRepository saleItemRepository;

    @Autowired
    EmployeeRepository employeeRepository;


    @Autowired
    ClientRepository clientRepository;

    @Test
    public void testMustSuccessfullySale() {

        // Create context
        Sale sale = Sale.builder().build();
        sale.setSaleItems(Arrays.asList(SaleItem.builder().build(), SaleItem.builder().build(), SaleItem.builder().build()));

        // Excecute
        Sale saleSaved = saleRepository.save(sale);
        Optional<Sale> result = saleRepository.findById(saleSaved.getId());
        List<SaleItem> saleItensSaved = saleItemRepository.findAll();

        // Verification
        Assertions.assertThat(result.get()).isEqualTo(saleSaved);
        Assertions.assertThat(saleItensSaved).hasSize(3);

    }

    @Test
    public void testDeleteCascade(){

        // Create context
        Sale sale = Sale.builder().build();
        sale.setSaleItems(Arrays.asList(SaleItem.builder().build(), SaleItem.builder().build(), SaleItem.builder().build()));

        // Excecute
        Sale saleSaved = saleRepository.save(sale);
        saleRepository.deleteById(saleSaved.getId());
        List<SaleItem> result = saleItemRepository.findAll();

        // Verification
        Assertions.assertThat(result).isEmpty();

    }

    @Test
    public void testMustReturnListOfSalesByEmployeeId() {

        Employee employee = Employee.builder().build();
        Employee employeeSaved = employeeRepository.save(employee);

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
    public void testMustReturnListOfSalesByClientId() {

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
    public void testMustReturnListOfSalesBySaleDate() {

        Sale saleOne = Sale.builder().saleDate(LocalDateTime.now()).build();
        Sale saleOneSaved = saleRepository.save(saleOne);

        Assertions.assertThat(saleOneSaved.getSaleDate()).isNotNull();
    }

    @Test
    public void testMustReturnListOfSalesByQuery() {

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
