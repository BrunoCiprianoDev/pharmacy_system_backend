package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Purchase;
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
public class PurchaseRepositoryTest {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Test
    public void testMustReturnListOfPurchaseByNoteNumberLikeQuery(){

        Purchase purchaseOne = Purchase.builder().noteNumber("NoteNumberString").build();
        purchaseRepository.save(purchaseOne);

        Purchase purchaseTwo = Purchase.builder().noteNumber("NoteNumberStringTwo").build();
        purchaseRepository.save(purchaseTwo);

        Purchase purchaseThree = Purchase.builder().noteNumber("NoteNumberStringThree").build();
        purchaseRepository.save(purchaseThree);

        List<Purchase> listResponse = purchaseRepository.findPurchasesByNoteNumberByQuery("Number");

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfPurchaseBySupplierId(){

        Supplier supplier = Supplier.builder().build();
        Supplier supplierSaved = supplierRepository.save(supplier);

        Purchase purchaseOne = Purchase.builder().supplier(supplier).build();
        purchaseRepository.save(purchaseOne);

        Purchase purchaseTwo = Purchase.builder().supplier(supplier).build();
        purchaseRepository.save(purchaseTwo);

        Purchase purchaseThree = Purchase.builder().supplier(supplier).build();
        purchaseRepository.save(purchaseThree);

        List<Purchase> listResponse = purchaseRepository.findPurchasesBySupplierId(supplierSaved.getId());

        Assertions.assertThat(listResponse).hasSize(3);

    }





}
