package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.*;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleRepository;
import com.bcipriano.pharmacysystem.service.impl.SaleServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

        // Exception if total invalid
        sale.setTotal(-1D);
        Throwable exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o total.");
        sale.setTotal(0D);

        // Exception if employee is NULL
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Funcionário inválido.");
        sale.setEmployee(Employee.builder().id(1l).build());

        // Exception if employee no exists
        Mockito.when(employeeRepository.existsById(Mockito.anyLong())).thenReturn(false);
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Funcionário inválido.");

        // Exception if client exists but is invalid
        Mockito.when(employeeRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(clientRepository.existsById(Mockito.anyLong())).thenReturn(false);
        sale.setClient(Client.builder().id(1l).build());
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Cliente inválido.");
        Mockito.when(clientRepository.existsById(Mockito.anyLong())).thenReturn(true);

        // Exception if saleItems does not have at least 1 item
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("É necessário existir pelo menos 1 item na venda.");

        // Exception if list sale itens have 1 invalid item
        sale.setSaleItems(Arrays.asList(SaleItem.builder().units(-1).build()));
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o número de unidades");

        // Exception if item sale have invalid sellPrice
        sale.setSaleItems(Arrays.asList(SaleItem.builder().units(0).sellPrice(-1d).build()));
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o valor de venda.");

        // Exception if SaleItem have invalid Lot
        sale.setSaleItems(Arrays.asList(SaleItem.builder().units(0).sellPrice(0d).lot(Lot.builder().id(1l).build()).build()));
        Mockito.when(lotRepository.existsById(Mockito.anyLong())).thenReturn(false);
        exception = Assertions.catchThrowable(() -> saleService.validateSale(sale));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Lote inválido.");

    }

}
