package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SupplierServiceTest {

    @SpyBean
    SupplierService supplierService;

    @MockBean
    SupplierRepository supplierRepository;

    @Test
    public void testValidateSupplier(){

        Supplier supplier = Supplier.builder().build();

        // Exception case name is NULL
        Throwable exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome do fornecedor é obrigatório.");
        supplier.setName("AnyString");

        // Exception case CNPJ is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CNPJ do fornecedor é obrigatório.");
        supplier.setCnpj("");

        // Exception case CNPJ invalid
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("CNPJ do fornecedor inválido.");
        supplier.setCnpj("00.000/0000-00");

        // Exception case Address is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Endereço do fornecedor inválido.");

        supplier.setAddress(Address.builder()
                .cep("00.000-000")
                .uf("XX")
                .city("AnyString")
                .neightborhood("AnyString")
                .addressDetail("AnyString")
                .number("AnyString")
                .complement("AnyString")
                .build());

        // Exception if primaryPhone is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone 1 do fornecedor é obrigatório.");
        supplier.setPrimaryPhone("");

        // Exception if primaryPhone not matches with pattern
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 1 do fornecedor inválido.");
        supplier.setPrimaryPhone("(00)00000-0000");

        // Exception if secundaryPhone not matches with pattern
        supplier.setSecundaryPhone("");
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 2 do fornecedor inválido.");
        supplier.setSecundaryPhone("(00)0000-0000");

        // Exception if e-mail is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo e-mail é do fornecedor obrigatório.");
        supplier.setEmail("");

        // Exception if e-mail not matches pattern
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("E-mail do fornecedor inválido.");
        supplier.setEmail("email@email.com");

    }

    @Test
    public void testSaveSupplierException() {

        Mockito.when(supplierRepository.existsByCnpj(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> supplierService.saveSupplier(Supplier.builder()
                .address(Address.builder().build())
                .build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

        Mockito.verify(supplierRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void testUpdateSupplierException() {

        Mockito.when(supplierRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> supplierService.updateSupplier(Supplier.builder()
                .address(Address.builder().build())
                .build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

        Mockito.verify(supplierRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void testGetSupplierByIdException() {

        Mockito.when(supplierRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(()-> supplierService.getSupplierById(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

    }

    @Test
    public void testDeleteSupplierException() {

        Mockito.when(supplierRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> supplierService.deleteSupplier(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

    }

}