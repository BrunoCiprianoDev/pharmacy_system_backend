package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleItemRepository;
import com.bcipriano.pharmacysystem.service.impl.SaleItemServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SaleItemServiceTest {

    @MockBean
    LotRepository lotRepository;

    @Test
    public void testValidateSaleItem(){

        SaleItem saleItem = SaleItem.builder().build();

        //Mockito.when(lotRepository.existsById(Mockito.anyLong())).thenReturn(true);

        // Exception if total invalid
        saleItem.setUnits(-1);
        Throwable exception = Assertions.catchThrowable(() -> SaleItemService.validateSaleItem(saleItem, lotRepository));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o número de unidades");
        saleItem.setUnits(0);

        // Exception if sellPrice invalid
        saleItem.setSellPrice(-1d);
        exception = Assertions.catchThrowable(() -> SaleItemService.validateSaleItem(saleItem, lotRepository));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o valor de venda.");
        saleItem.setSellPrice(0d);

        // Exception if lot is NULL
        exception = Assertions.catchThrowable(() -> SaleItemService.validateSaleItem(saleItem, lotRepository));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Lote inválido.");
        saleItem.setLot(Lot.builder().id(1l).build());

        // Exception if existsById == NULL
        Mockito.when(lotRepository.existsById(Mockito.anyLong())).thenReturn(false);
        exception = Assertions.catchThrowable(() -> SaleItemService.validateSaleItem(saleItem, lotRepository));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Lote inválido.");

    }



}
