package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Lot;
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

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class SaleItemRepositoryTest {

    @Autowired
    SaleItemRepository saleItemRepository;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    LotRepository lotRepository;

    @Test
    public void testMustResturnSaleItemBySaleId(){

        Sale sale = Sale.builder().build();
        Sale saleSaved = saleRepository.save(sale);

        SaleItem saleItemOne = SaleItem.builder().sale(saleSaved).build();
        saleItemRepository.save(saleItemOne);

        SaleItem saleItemTwo = SaleItem.builder().sale(saleSaved).build();
        saleItemRepository.save(saleItemTwo);

        List<SaleItem> listResponse = saleItemRepository.findSaleItemsBySaleId(saleSaved.getId());

        Assertions.assertThat(listResponse).hasSize(2);
        System.out.println(listResponse);

    }

    @Test
    public void testMustResturnSaleItemByLotId(){

        Lot lot = Lot.builder().build();
        Lot lotSaved = lotRepository.save(lot);

        SaleItem saleItemOne = SaleItem.builder().lot(lotSaved).build();
        saleItemRepository.save(saleItemOne);

        SaleItem saleItemTwo = SaleItem.builder().lot(lotSaved).build();
        saleItemRepository.save(saleItemTwo);

        List<SaleItem> listResponse = saleItemRepository.findSaleItemByLotId(lotSaved.getId());

        Assertions.assertThat(listResponse).hasSize(2);

    }


}
