package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class DiscountGroupRepositoryTest {

    @Autowired
    DiscountGroupRepository discountGroupRepository;

    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void testDiscountGroupByQuery() {

        DiscountGroup discountGroup1 = createDiscountGroup("DiscountGroupOne");
        DiscountGroup discountGroup2 = createDiscountGroup("DiscountGroupTwo");
        DiscountGroup discountGroup3 = createDiscountGroup("DiscountGroupThree");
        DiscountGroup discountGroup4 = createDiscountGroup("DiscountGroupFour");

        DiscountGroup discountGroup1Saved = discountGroupRepository.save(discountGroup1);
        discountGroupRepository.save(discountGroup2);
        discountGroupRepository.save(discountGroup3);
        discountGroupRepository.save(discountGroup4);

        Page<DiscountGroup> result1 = discountGroupRepository.findDiscountGroupByQuery("DiscountGroup", pageable);
        Assertions.assertThat(result1.getContent()).hasSize(4);

        Page<DiscountGroup> result2 = discountGroupRepository.findDiscountGroupByQuery("One", pageable);
        Assertions.assertThat(result2.getContent().get(0)).isEqualTo(discountGroup1Saved);

    }

    @Test
    public void testDiscountGroupByStartDate() {

        String statDate = "2023-04-14";
        String finalDate = "2024-04-14";

        DiscountGroup discountGroup = createDiscountGroup(statDate, finalDate);

        DiscountGroup discountGroupSaved = discountGroupRepository.save(discountGroup);

        Page<DiscountGroup> result = discountGroupRepository.findByStartDate(discountGroupSaved.getStartDate(), pageable);

        Assertions.assertThat(result.getContent().get(0)).isEqualTo(discountGroupSaved);

    }

    @Test
    public void testDiscountGroupByFinalDate() {

        String statDate = "2023-04-14";
        String finalDate = "2024-04-14";

        DiscountGroup discountGroup = createDiscountGroup(statDate, finalDate);

        DiscountGroup discountGroupSaved = discountGroupRepository.save(discountGroup);

        Page<DiscountGroup> result = discountGroupRepository.findByFinalDate(discountGroupSaved.getFinalDate(), pageable);

        Assertions.assertThat(result.getContent().get(0)).isEqualTo(discountGroupSaved);

    }

    private DiscountGroup createDiscountGroup(String name) {
        return DiscountGroup.builder().name(name).build();
    }

    private DiscountGroup createDiscountGroup(String startDate, String finalDate) {
        return DiscountGroup
                .builder()
                .startDate(LocalDate.parse(startDate))
                .finalDate(LocalDate.parse(finalDate))
                .build();
    }

}
