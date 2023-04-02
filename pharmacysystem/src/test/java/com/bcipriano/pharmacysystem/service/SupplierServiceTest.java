package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;
import com.bcipriano.pharmacysystem.service.impl.SupplierServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SupplierServiceTest {

    @SpyBean
    SupplierServiceImpl supplierService;

    @MockBean
    SupplierRepository supplierRepository;

    @Test
    public void testValidateSupplier(){

        Supplier supplier = Supplier.builder().build();

        // Exception case name is NULL
        Throwable exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome é obrigatório.");
        supplier.setName("AnyString");

        // Exception case CNPJ is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CNPJ é obrigatório.");
        supplier.setCnpj("");

        // Exception case CNPJ invalid
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("CNPJ inválido.");
        supplier.setCnpj("00.000/0000-00");

        // Exception case Address is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Endereço inválido.");

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
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone 1 é obrigatório.");
        supplier.setPrimaryPhone("");

        // Exception if primaryPhone not matches with pattern
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 1 inválido.");
        supplier.setPrimaryPhone("(00)00000-0000");

        // Exception if secundaryPhone not matches with pattern
        supplier.setSecundaryPhone("");
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone 2 inválido.");
        supplier.setSecundaryPhone("(00)0000-0000");

        // Exception if e-mail is NULL
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo e-mail é obrigatório.");
        supplier.setEmail("");

        // Exception if e-mail not matches pattern
        exception = Assertions.catchThrowable(() -> supplierService.validateSupplier(supplier));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("E-mail inválido.");
        supplier.setEmail("email@email.com");

    }

}
