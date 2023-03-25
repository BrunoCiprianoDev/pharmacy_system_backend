package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class LossRepositoryTest {

    @Autowired
    LossRepository lossRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    LotRepository lotRepository;

    @Test
    public void testMustReturnListOfLossesByNameMerchandise(){

        Employee employeeOne = Employee.builder().name("EmployeeOne").build();
        Employee employeeOneSave = employeeRepository.save(employeeOne);

        Employee employeeTwo = Employee.builder().name("EmployeeTwo").build();
        Employee employeeTwoSave = employeeRepository.save(employeeTwo);

        Loss lossOne = Loss.builder().employee(employeeOneSave).build();
        lossRepository.save(lossOne);

        Loss lossTwo = Loss.builder().employee(employeeTwoSave).build();
        lossRepository.save(lossTwo);

        List<Loss> listResponse = lossRepository.findByEmployeeName("Employee");

        Assertions.assertThat(listResponse).hasSize(2);

    }

    @Test
    public void testMustReturnLossByLotId(){

        Lot lotOne = Lot.builder().build();
        Lot lotOneSave = lotRepository.save(lotOne);

        Loss lossOne = Loss.builder().lot(lotOneSave).build();
        lossRepository.save(lossOne);

        Loss lossTwo = Loss.builder().lot(lotOneSave).build();
        lossRepository.save(lossTwo);

        List<Loss> listResponse = lossRepository.findLossByLotId(lotOneSave.getId());

        Assertions.assertThat(listResponse).hasSize(2);

    }

    @Test
    public void testMustReturnLossLotNumber(){

        Lot lotOne = Lot.builder().number("StringNumber").build();
        Lot lotOneSave = lotRepository.save(lotOne);

        Loss lossOne = Loss.builder().lot(lotOneSave).build();
        lossRepository.save(lossOne);

        Loss lossTwo = Loss.builder().lot(lotOneSave).build();
        lossRepository.save(lossTwo);

        List<Loss> listResponse = lossRepository.findLossByLotNumber("Number");

        Assertions.assertThat(listResponse).hasSize(2);

    }

}
