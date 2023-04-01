package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.repository.DiscountGroupRepository;
import com.bcipriano.pharmacysystem.service.impl.DiscountGroupServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DiscountGroupServiceTest {

    @SpyBean
    DiscountGroupServiceImpl discountGroupService;

    @MockBean
    DiscountGroupRepository discountGroupRepository;

    @Test
    public void testValidateDiscountGroup(){

        DiscountGroup discountGroup = DiscountGroup.builder().build();

        // Exception case name is NULL
        Throwable exception = Assertions.catchThrowable(()-> discountGroupService.validateDiscountGroup(discountGroup));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome é obrigatório.");
        discountGroup.setName("AnyString");

        // Exception case startDate is NULL
        exception = Assertions.catchThrowable(()-> discountGroupService.validateDiscountGroup(discountGroup));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo data inicial é obrigatório.");
        discountGroup.setStartDate(LocalDate.of(2023, 1, 1));

        // Exception case finalDate is NULL
        exception = Assertions.catchThrowable(()-> discountGroupService.validateDiscountGroup(discountGroup));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo data final é obrigatório.");
        discountGroup.setFinalDate(LocalDate.of(2023, 1, 1));

        // Exception case parcentage <= 0
        discountGroup.setPercentage(-1d);
        exception = Assertions.catchThrowable(()-> discountGroupService.validateDiscountGroup(discountGroup));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O percentual deve estár entre 0 e 100%.");

        // Exception case percentage > 100
        discountGroup.setPercentage(101d);
        exception = Assertions.catchThrowable(()-> discountGroupService.validateDiscountGroup(discountGroup));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O percentual deve estár entre 0 e 100%.");
        discountGroup.setPercentage(99d);

        // Exception case minimumUnits < 0
        discountGroup.setMinimumUnits(-1);
        exception = Assertions.catchThrowable(()-> discountGroupService.validateDiscountGroup(discountGroup));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O valor para unidades mínimas deve ser maior que zero.");

    }

    @Test
    public void testExceptionDiscountGroup(){

        Mockito.when(discountGroupRepository.existsByName(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> discountGroupService.saveDiscountGroup(DiscountGroup.builder().name(Mockito.anyString()).build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Já existe um grupo de desconto com esse nome.");

    }

    @Test
    public void testExceptionUpdateDiscountGroup(){

        Mockito.when(discountGroupRepository.existsByName(Mockito.anyString())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> discountGroupService.updateDiscountGroup(DiscountGroup.builder().name(Mockito.anyString()).build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O desconto que está tentando modificar não existe.");

    }

    @Test
    public void testExceptionDiscountGroupById(){

        Mockito.when(discountGroupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(()-> discountGroupService.getDiscountGroupById(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

    }

    @Test
    public void testExceptionDeleteDiscountGroup(){

        Mockito.when(discountGroupRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> discountGroupService.deleteDiscountGroup(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

    }

}
