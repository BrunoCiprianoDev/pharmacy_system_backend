package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
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

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class SupplierRepositoryTest {

    @Autowired
    SupplierRepository supplierRepository;

    @Test
    public void testFindSupplierByQuery() {

        Pageable pageable = PageRequest.of(0, 10);

        Supplier supplier1 = Supplier.builder()
                .name("Supplier1")
                .cnpj("111.1111.1111.11")
                .primaryPhone("(11)1111-1111")
                .secundaryPhone("(12)1111-1111")
                .email("emailSupplier1@email.com")
                .build();

        Supplier supplier2 = Supplier.builder()
                .name("Supplier2")
                .cnpj("222.222.222.22")
                .primaryPhone("(22)2222-2222")
                .secundaryPhone("(21)2222-2222")
                .email("emailSupplier2@email.com")
                .build();

        Supplier supplier1Saved = supplierRepository.save(supplier1);
        Supplier supplier2Saved = supplierRepository.save(supplier2);

        Page<Supplier> result1 = supplierRepository.findSupplierByQuery("Supplier", pageable);
        Assertions.assertThat(result1.getContent()).hasSize(2);

        //Get Supplier by email
        Page<Supplier> result2 = supplierRepository.findSupplierByQuery("email", pageable);
        Assertions.assertThat(result2.getContent()).hasSize(2);

        //Get Supplier1 by cpf
        Page<Supplier> result3 = supplierRepository.findSupplierByQuery("111.1111.1111.11", pageable);
        Assertions.assertThat(result2.getContent().get(0)).isEqualTo(supplier1Saved);

        //Get Supplier2 by phone1
        Page<Supplier> result4 = supplierRepository.findSupplierByQuery("(22)2222-2222", pageable);
        Assertions.assertThat(result4.getContent().get(0)).isEqualTo(supplier2Saved);

    }

    @Test
    public void existsByIdExceptionTest() {

        Supplier supplier = Supplier.builder()
                .name("Supplier")
                .cnpj("111.1111.1111.11")
                .primaryPhone("(11)1111-1111")
                .secundaryPhone("(12)1111-1111")
                .email("emailSupplier@email.com")
                .build();

        Supplier supplierSaved = supplierRepository.save(supplier);

        boolean result = supplierRepository.existsById(supplierSaved.getId());

        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void existsByCnpjExceptionTest() {

        Supplier supplier = Supplier.builder()
                .name("Supplier")
                .cnpj("111.1111.1111.11")
                .primaryPhone("(11)1111-1111")
                .secundaryPhone("(12)1111-1111")
                .email("emailSupplier@email.com")
                .build();

        Supplier supplierSaved = supplierRepository.save(supplier);

        boolean result = supplierRepository.existsByCnpj(supplierSaved.getCnpj());

        Assertions.assertThat(result).isTrue();

    }

}
