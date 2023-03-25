package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
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
public class DiscountGroupRepositoryTest {

    @Autowired
    DiscountGroupRepository discountGroupRepository;

    @Test
    public void testMustReturnGroupDiscountByQuery(){

        //Create context
        DiscountGroup discountGroupOne = DiscountGroup.builder().name("NameDiscountOne").build();
        discountGroupRepository.save(discountGroupOne);

        DiscountGroup discountGroupTwo = DiscountGroup.builder().name("NameDiscountTwo").build();
        discountGroupRepository.save(discountGroupTwo);

        DiscountGroup discountGroupThree = DiscountGroup.builder().name("NameDiscountThree").build();
        discountGroupRepository.save(discountGroupThree);

        //Execute
        List<DiscountGroup> listResponse = discountGroupRepository.findDiscountGroupByQuery("Discount");

        //Verification
        Assertions.assertThat(listResponse).hasSize(3);

    }

    @Test
    public void testMustReturnAListOfDiscountsByStartDate() {

        //Create context
        DiscountGroup discountOne = DiscountGroup.builder().startDate(LocalDate.of(2023, 3, 2)).build();
        discountGroupRepository.save(discountOne);

        DiscountGroup discountGroupTwo = DiscountGroup.builder().startDate(LocalDate.of(2023, 3, 2)).build();
        discountGroupRepository.save(discountGroupTwo);

        //Execute
        List<DiscountGroup> listResponse = discountGroupRepository.findByStartDate(LocalDate.of(2023, 3, 2));

        //Verification
        Assertions.assertThat(listResponse).hasSize(2);

    }

    @Test
    public void testMustReturnAListOfDiscountsByFinalDate() {

        //Create context
        DiscountGroup discountOne = DiscountGroup.builder().finalDate(LocalDate.of(2023, 3, 2)).build();
        discountGroupRepository.save(discountOne);

        DiscountGroup discountGroupTwo = DiscountGroup.builder().finalDate(LocalDate.of(2023, 3, 2)).build();
        discountGroupRepository.save(discountGroupTwo);

        //Execute
        List<DiscountGroup> listResponse = discountGroupRepository.findByFinalDate(LocalDate.of(2023, 3, 2));

        //Verification
        Assertions.assertThat(listResponse).hasSize(2);

    }


}
