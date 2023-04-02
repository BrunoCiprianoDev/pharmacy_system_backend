package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleRepository;
import com.bcipriano.pharmacysystem.service.impl.SaleServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SaleServiceTest {

    @SpyBean
    SaleServiceImpl saleService;

    @MockBean
    SaleRepository saleRepository;

    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    ClientRepository clientRepository;

    @MockBean
    LotRepository lotRepository;


    @Test
    public void testValidateSale(){

        Sale sale = Sale.builder().build();
        sale.setSaleItems(Arrays.asList(SaleItem.builder().build(), SaleItem.builder().build(), SaleItem.builder().build()));

        sale.setTotal(-1D);
        Throwable exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o total.");
        sale.setTotal(0D);

        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Funcionário inválido.");



    }

}
