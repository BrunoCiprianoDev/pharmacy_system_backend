package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import com.bcipriano.pharmacysystem.model.repository.MerchandiseRepository;
import com.bcipriano.pharmacysystem.service.impl.MerchandiseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MerchandiseServiceTest {

    @SpyBean
    MerchandiseServiceImpl merchandiseService;

    @MockBean
    MerchandiseRepository merchandiseRepository;

    @Test
    public void testMerchandiseValidate(){

        Merchandise merchandise = Merchandise.builder().build();

        // Exception if name is null or ""
        Throwable exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome é obrigatório.");
        merchandise.setName("AnyString");

        // Exception if code is null
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo código de barras é obrigatório.");
        merchandise.setCode("");

        // Exception if code.length() != 12
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O codigo de barras inválido.(Verifique os 12 digitos)");
        merchandise.setCode("000000000000");

        // Exception if department is NULL
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Departamento inválido.");
        merchandise.setDepartment(Department.BEAUTY);

        // Exception if brand is NULL or ""
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo marca é obrigatório.");
        merchandise.setBrand("AnyString");

        // Exception if stripe is NULL
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Tarja inválida.");
        merchandise.setStripe(Stripe.NO_STRIPE);

        // Exception if minimumStock < 0
        merchandise.setMinimumStock(-1);
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o estoque mínimo.");
        merchandise.setMinimumStock(0);

        // Exception if currentStock < 0
        merchandise.setCurrentStock(-1);
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o estoque atual.");
        merchandise.setCurrentStock(0);

        // Exception if maximunStock < 0
        merchandise.setMaximumStock(-1);
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o estoque máximo.");
        merchandise.setMaximumStock(0);

        // Exception if fullPrice < 0
        merchandise.setFullPrice(-1D);
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o preço inteiro.");
        merchandise.setFullPrice(1D);

        // Exception if comission invalid
        merchandise.setComission(-1D);
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o percentual de comissão.");
        merchandise.setComission(1D);

        // Exception if pmc is not valid
        merchandise.setPmc(-1D);
        exception = Assertions.catchThrowable(() -> merchandiseService.validateMerchandise(merchandise));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Valor inválido para o preço máximo permitido ao consumidor.");

    }

}
