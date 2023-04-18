package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.StorageTemperature;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class MerchandiseRepositoryTest {

    @Autowired
    MerchandiseRepository merchandiseRepository;

    @Autowired
    DiscountGroupRepository discountGroupRepository;

    public final Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void testFindMerchandiseByQuery() {

        DiscountGroup discountGroup = DiscountGroup.builder().name("DiscountGroup").build();
        DiscountGroup discountGroupSaved = discountGroupRepository.save(discountGroup);

        Merchandise merchandise1 = Merchandise.builder()
                .name("Name1Merchandise1")
                .code("12345550123")
                .department(Department.DERMATOLOGICAL)
                .classification("classification1Merchadise1")
                .brand("Brand1Merchandise1")
                .formule("Formule1Merchandise1")
                .storageTemperature(StorageTemperature.ABOVE_20)
                .stripe(Stripe.NO_STRIPE)
                .minimumStock(100)
                .currentStock(0)
                .maximumStock(250)
                .fullPrice(25.99)
                .comission(25.00)
                .pmc(28.99)
                .description("Merchandise1 description.")
                .discountGroup(discountGroupSaved)
                .build();

        Merchandise merchandise2 = Merchandise.builder()
                .name("Name2Merchandise2")
                .code("1236660123")
                .department(Department.DERMATOLOGICAL)
                .classification("classification2Merchadise2")
                .brand("Brand2Merchandise2")
                .formule("Formule2Merchandise2")
                .storageTemperature(StorageTemperature.BELOW_NEGATIVE_10)
                .stripe(Stripe.RED)
                .minimumStock(100)
                .currentStock(0)
                .maximumStock(250)
                .fullPrice(25.99)
                .comission(25.00)
                .pmc(28.99)
                .description("Merchandise2 description.")
                .discountGroup(discountGroupSaved)
                .build();

        Merchandise merchandise1Saved = merchandiseRepository.save(merchandise1);
        Merchandise merchandise2Saved = merchandiseRepository.save(merchandise2);

        // Search merchandise by name
        Page<Merchandise> result1 = merchandiseRepository.findMerchandiseByQuery("Name", pageable);
        Assertions.assertThat(result1.getContent()).hasSize(2);

        // Find merchandise1 by code
        Page<Merchandise> result2 = merchandiseRepository.findMerchandiseByQuery("555", pageable);
        Assertions.assertThat(result2.getContent().get(0)).isEqualTo(merchandise1Saved);
        Assertions.assertThat(result2.getContent()).hasSize(1);

        // Find merchandise by classifications
        Page<Merchandise> result3 = merchandiseRepository.findMerchandiseByQuery("classification", pageable);
        Assertions.assertThat(result3.getContent()).hasSize(2);

        // Find merchandise by brand
        Page<Merchandise> result4 = merchandiseRepository.findMerchandiseByQuery("Brand", pageable);
        Assertions.assertThat(result4.getContent()).hasSize(2);

        // Find merchandise by formule
        Page<Merchandise> result5 = merchandiseRepository.findMerchandiseByQuery("Formule", pageable);
        Assertions.assertThat(result5.getContent()).hasSize(2);

        List<List> ListMain = new ArrayList<>();
        List<Object> primaryList = new ArrayList<>();
        List<Object> secundaryList = new ArrayList<>();

        ListMain.add(primaryList);
        ListMain.add(secundaryList);

    }

    @Test
    public void testFindByCode() {

        Merchandise merchandise1 = Merchandise.builder().code("1236660123").build();

        Merchandise merchandise2 = Merchandise.builder().code("1235550123").build();

        Merchandise merchandise1Saved = merchandiseRepository.save(merchandise1);
        Merchandise merchandise2Saved = merchandiseRepository.save(merchandise2);

        Optional<Merchandise> result1 = merchandiseRepository.findByCode(merchandise1.getCode());
        Assertions.assertThat(result1.get()).isEqualTo(merchandise1Saved);

        Optional<Merchandise> result2 = merchandiseRepository.findByCode(merchandise2Saved.getCode());
        Assertions.assertThat(result2.get()).isNotEqualTo(merchandise1Saved);

    }

    @Test
    public void testFindByDepartment() {

        Merchandise merchandise1 = Merchandise.builder().department(Department.DERMATOLOGICAL).build();
        Merchandise merchandise2 = Merchandise.builder().department(Department.DERMATOLOGICAL).build();
        Merchandise merchandise3 = Merchandise.builder().department(Department.CHILDREN).build();
        Merchandise merchandise4 = Merchandise.builder().department(Department.CHILDREN).build();
        Merchandise merchandise5 = Merchandise.builder().department(Department.CHILDREN).build();

        merchandiseRepository.save(merchandise1);
        merchandiseRepository.save(merchandise2);
        merchandiseRepository.save(merchandise3);
        merchandiseRepository.save(merchandise4);
        merchandiseRepository.save(merchandise5);

        Page<Merchandise> result1 = merchandiseRepository.findByDepartment(Department.DERMATOLOGICAL, pageable);
        Assertions.assertThat(result1.getContent()).hasSize(2);

        Page<Merchandise> result2 = merchandiseRepository.findByDepartment(Department.CHILDREN, pageable);
        Assertions.assertThat(result2.getContent()).hasSize(3);

    }

    @Test
    public void testFindByStripe() {

        Merchandise merchandise1 = Merchandise.builder().stripe(Stripe.NO_STRIPE).build();
        Merchandise merchandise2 = Merchandise.builder().stripe(Stripe.NO_STRIPE).build();
        Merchandise merchandise3 = Merchandise.builder().stripe(Stripe.NO_STRIPE).build();
        Merchandise merchandise4 = Merchandise.builder().stripe(Stripe.RED).build();
        Merchandise merchandise5 = Merchandise.builder().stripe(Stripe.RED).build();

        merchandiseRepository.save(merchandise1);
        merchandiseRepository.save(merchandise2);
        merchandiseRepository.save(merchandise3);
        merchandiseRepository.save(merchandise4);
        merchandiseRepository.save(merchandise5);

        Page<Merchandise> result1 = merchandiseRepository.findByStripe(Stripe.RED, pageable);
        Assertions.assertThat(result1.getContent()).hasSize(2);

        Page<Merchandise> result2 = merchandiseRepository.findByStripe(Stripe.NO_STRIPE, pageable);
        Assertions.assertThat(result2.getContent()).hasSize(3);

    }

    @Test
    public void testByDiscountGroupId() {

        DiscountGroup discountGroup1 = DiscountGroup.builder().name("DiscountGroup1").build();
        DiscountGroup discountGroup1Saved = discountGroupRepository.save(discountGroup1);

        DiscountGroup discountGroup2 = DiscountGroup.builder().name("DiscountGroup2").build();
        DiscountGroup discountGroup2Saved = discountGroupRepository.save(discountGroup2);

        Merchandise merchandise1 = Merchandise.builder().discountGroup(discountGroup1Saved).build();
        Merchandise merchandise2 = Merchandise.builder().discountGroup(discountGroup1Saved).build();
        Merchandise merchandise3 = Merchandise.builder().discountGroup(discountGroup1Saved).build();
        Merchandise merchandise4 = Merchandise.builder().discountGroup(discountGroup2Saved).build();
        Merchandise merchandise5 = Merchandise.builder().discountGroup(discountGroup2Saved).build();

        merchandiseRepository.save(merchandise1);
        merchandiseRepository.save(merchandise2);
        merchandiseRepository.save(merchandise3);
        merchandiseRepository.save(merchandise4);
        merchandiseRepository.save(merchandise5);

        Page<Merchandise> result1 = merchandiseRepository.findByDiscountGroupId(discountGroup1.getId(), pageable);
        Assertions.assertThat(result1.getContent()).hasSize(3);

    }

    @Test
    public void testFindByDiscountGroupName() {

        DiscountGroup discountGroup1 = DiscountGroup.builder().name("DiscountGroup1").build();
        DiscountGroup discountGroup1Saved = discountGroupRepository.save(discountGroup1);

        DiscountGroup discountGroup2 = DiscountGroup.builder().name("DiscountGroup2").build();
        DiscountGroup discountGroup2Saved = discountGroupRepository.save(discountGroup2);

        Merchandise merchandise1 = Merchandise.builder().discountGroup(discountGroup1Saved).build();
        Merchandise merchandise2 = Merchandise.builder().discountGroup(discountGroup1Saved).build();
        Merchandise merchandise3 = Merchandise.builder().discountGroup(discountGroup1Saved).build();
        Merchandise merchandise4 = Merchandise.builder().discountGroup(discountGroup2Saved).build();
        Merchandise merchandise5 = Merchandise.builder().discountGroup(discountGroup2Saved).build();

        merchandiseRepository.save(merchandise1);
        merchandiseRepository.save(merchandise2);
        merchandiseRepository.save(merchandise3);
        merchandiseRepository.save(merchandise4);
        merchandiseRepository.save(merchandise5);

        Page<Merchandise> result1 = merchandiseRepository.findByDiscountGroupName("DiscountGroup", pageable);
        Assertions.assertThat(result1.getContent()).hasSize(5);

    }

}
