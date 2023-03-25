package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
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
public class LotRepositoryTest {

    @Autowired
    LotRepository lotRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    MerchandiseRepository merchandiseRepository;

    @Test
    public void testMustReturnListOfLotsByPurchaseId(){

        Purchase purchase = Purchase.builder().build();
        Purchase purchaseSave = purchaseRepository.save(purchase);

        Lot lotOne = Lot.builder().purchase(purchaseSave).build();
        lotRepository.save(lotOne);

        Lot lotTwo = Lot.builder().purchase(purchaseSave).build();
        lotRepository.save(lotTwo);

        Lot lotThree = Lot.builder().purchase(purchaseSave).build();
        lotRepository.save(lotThree);

        List<Lot> listResponse = lotRepository.findLotsByPurchaseId(purchaseSave.getId());

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfLotsByMerchandiseId(){

        Merchandise merchandise = Merchandise.builder().build();
        Merchandise merchandiseSaved = merchandiseRepository.save(merchandise);

        Lot lotOne = Lot.builder().merchandise(merchandiseSaved).build();
        lotRepository.save(lotOne);

        Lot lotTwo = Lot.builder().merchandise(merchandiseSaved).build();
        lotRepository.save(lotTwo);

        Lot lotThree = Lot.builder().merchandise(merchandiseSaved).build();
        lotRepository.save(lotThree);

        List<Lot>listResponse = lotRepository.findLotsByMerchandiseId(merchandiseSaved.getId());

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfLotsByPurchaseNoteNumberLikeQuery(){

        Purchase purchase = Purchase.builder().noteNumber("NoteNumberString").build();
        purchaseRepository.save(purchase);

        Lot lotOne = Lot.builder().purchase(purchase).build();
        lotRepository.save(lotOne);

        Lot lotTwo = Lot.builder().purchase(purchase).build();
        lotRepository.save(lotTwo);

        Lot lotThree = Lot.builder().purchase(purchase).build();
        lotRepository.save(lotThree);

        List<Lot> listResponse = lotRepository.findLotsByPurchaseNoteNumber("Number");

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testReturnLotByNumberLikeQuery(){

        Lot lotOne = Lot.builder().number("LotNumberOne").build();
        lotRepository.save(lotOne);

        Lot lotTwo = Lot.builder().number("LotNumberTwo").build();
        lotRepository.save(lotTwo);

        Lot lotThree = Lot.builder().number("LotNumberThree").build();
        lotRepository.save(lotThree);

        List<Lot> listResponse = lotRepository.findLotsByLotNumber("Number");

        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnListOfLotByExpirationDate(){

        Lot lotOne = Lot.builder().expirationDate(LocalDate.of(2023, 1, 1)).build();
        lotRepository.save(lotOne);

        Lot lotTwo = Lot.builder().expirationDate(LocalDate.of(2023,01,01)).build();
        lotRepository.save(lotTwo);

        Lot lotThree = Lot.builder().expirationDate(LocalDate.of(2023, 1, 1)).build();
        lotRepository.save(lotThree);

        List<Lot>listResponse = lotRepository.findByExpirationDate(LocalDate.of(2023, 01,01));

        Assertions.assertThat(listResponse).hasSize(3);

    }

}
