package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
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
public class SupplierRepositoryTest {

    @Autowired
    SupplierRepository supplierRepository;

    @Test
    public void testMustReturnListOfSupplierByQuery() {

        Supplier supplierOne = Supplier.builder().email("SupplierEmail").build();
        supplierRepository.save(supplierOne);

        Supplier supplierTwo = Supplier.builder().name("SupplierName").build();
        supplierRepository.save(supplierTwo);

        Supplier supplierThree = Supplier.builder().cnpj("SupplierCNPJ").build();
        supplierRepository.save(supplierThree);

        Supplier supplierFour = Supplier.builder().primaryPhone("PrimaryPhoneSupplier").build();
        supplierRepository.save(supplierFour);

        Supplier supplierFive = Supplier.builder().secundaryPhone("SecundaryPhoneSupplier").build();
        supplierRepository.save(supplierFive);

        List<Supplier> listResponse = supplierRepository.findSupplierByQuery("Supplier");

        Assertions.assertThat(listResponse).hasSize(5);

    }



}
