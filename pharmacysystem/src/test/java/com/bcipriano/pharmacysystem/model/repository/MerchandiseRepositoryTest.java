package com.bcipriano.pharmacysystem.model.repository;


import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class MerchandiseRepositoryTest {

    @Autowired
    MerchandiseRepository merchandiseRepository;

    @Autowired
    DiscountGroupRepository discountGroupRepository;

    @Test
    public void testMustReturnMerchandiseByDepartment() {
        //Create context
        Merchandise merchandiseOne = Merchandise.builder().department(Department.BEAUTY).build();
        merchandiseRepository.save(merchandiseOne);

        Merchandise merchandiseTwo = Merchandise.builder().department(Department.BEAUTY).build();
        merchandiseRepository.save(merchandiseTwo);

        Merchandise merchandiseThree = Merchandise.builder().department(Department.DERMATOLOGICAL).build();
        merchandiseRepository.save(merchandiseThree);

        //Execute
        List<Merchandise> listMerchandiseTypeOfBeauty = merchandiseRepository.findByDepartment(Department.BEAUTY);
        List<Merchandise> listMerchandiseTypeOfDermatological = merchandiseRepository.findByDepartment(Department.DERMATOLOGICAL);

        //Verification
        Assertions.assertThat(listMerchandiseTypeOfBeauty).hasSize(2);
        Assertions.assertThat(listMerchandiseTypeOfDermatological).hasSize(1);

    }

    @Test
    public void testMustReturnMerchandiseByStripe() {

        //Create context
        Merchandise merchandiseOne = Merchandise.builder().stripe(Stripe.NO_STRIPE).build();
        merchandiseRepository.save(merchandiseOne);

        Merchandise merchandiseTwo = Merchandise.builder().stripe(Stripe.NO_STRIPE).build();
        merchandiseRepository.save(merchandiseTwo);

        Merchandise merchandiseThree = Merchandise.builder().stripe(Stripe.RED).build();
        merchandiseRepository.save(merchandiseThree);

        Merchandise merchandiseFour = Merchandise.builder().stripe(Stripe.BLACK).build();
        merchandiseRepository.save(merchandiseFour);

        //Execute
        List<Merchandise> listResponseNO_STRIPE = merchandiseRepository.findByStripe(Stripe.NO_STRIPE);
        List<Merchandise> listResponseRED_STRIPE = merchandiseRepository.findByStripe(Stripe.RED);
        List<Merchandise> listResponseBLACK_STRIPE = merchandiseRepository.findByStripe(Stripe.BLACK);

        //Verification
        Assertions.assertThat(listResponseNO_STRIPE).hasSize(2);
        Assertions.assertThat(listResponseRED_STRIPE).hasSize(1);
        Assertions.assertThat(listResponseBLACK_STRIPE).hasSize(1);
    }

    @Test
    public void testMustReturnListOfMerchandisesByDiscountGroupId() {

        //Main Context
        DiscountGroup discountGroupOne = DiscountGroup.builder().build();
        DiscountGroup discountGroupOneSave = discountGroupRepository.save(discountGroupOne);

        Merchandise merchandiseOne = Merchandise.builder().discountGroup(discountGroupOneSave).build();
        merchandiseRepository.save(merchandiseOne);

        Merchandise merchandiseTwo = Merchandise.builder().discountGroup(discountGroupOneSave).build();
        merchandiseRepository.save(merchandiseTwo);

        //Control group
        DiscountGroup discountGroupTwo = DiscountGroup.builder().build();
        DiscountGroup discountGroupTwoSave = discountGroupRepository.save(discountGroupTwo);

        Merchandise merchandiseThree = Merchandise.builder().discountGroup(discountGroupTwoSave).build();
        merchandiseRepository.save(merchandiseThree);

        //Execute
        List<Merchandise> listReponse = merchandiseRepository.findByDiscountGroupId(discountGroupOneSave.getId());


        //Verification context
        Assertions.assertThat(listReponse).hasSize(2);
    }

    @Test
    public void testShouldReturnAListOfMerchandisesByDiscountGroupNameFromQuery() {

        //Main Context
        DiscountGroup discountGroupOne = DiscountGroup.builder().name("DiscountGroupName").build();
        DiscountGroup discountGroupOneSave = discountGroupRepository.save(discountGroupOne);

        Merchandise merchandiseOne = Merchandise.builder().discountGroup(discountGroupOneSave).build();
        merchandiseRepository.save(merchandiseOne);

        Merchandise merchandiseTwo = Merchandise.builder().discountGroup(discountGroupOneSave).build();
        merchandiseRepository.save(merchandiseTwo);

        //Control group
        DiscountGroup discountGroupControl = DiscountGroup.builder().name("AnyString").build();
        DiscountGroup discountGroupControlSave = discountGroupRepository.save(discountGroupControl);

        Merchandise merchandiseThree = Merchandise.builder().discountGroup(discountGroupControlSave).build();
        merchandiseRepository.save(merchandiseThree);

        //Execute
        List<Merchandise> listReponse = merchandiseRepository.findByDiscountGroupName("Group");

        //Verification
        Assertions.assertThat(listReponse).hasSize(2);
    }

    @Test
    public void testMustReturnMerchandisesByQuery() {

        //Create context
        Merchandise merchandiseOne = Merchandise.builder().name("NameMerchandise").build();
        merchandiseRepository.save(merchandiseOne);

        Merchandise merchandiseTwo = Merchandise.builder().code("CodeMerchandise").build();
        merchandiseRepository.save(merchandiseTwo);

        Merchandise merchandiseThree = Merchandise.builder().classification("ClassificationMerchandise").build();
        merchandiseRepository.save(merchandiseThree);

        Merchandise merchandiseFour = Merchandise.builder().brand("BrandMerchandise").build();
        merchandiseRepository.save(merchandiseFour);

        Merchandise merchandiseFive = Merchandise.builder().formule("FormuleMerchandise").build();
        merchandiseRepository.save(merchandiseFive);

        Merchandise merchandiseSix = Merchandise.builder().rms("RMSMerchandise").build();
        merchandiseRepository.save(merchandiseSix);

        //Execute
        List<Merchandise> listResponse = merchandiseRepository.findMerchandiseByQuery("Merchandise");

        //Verification
        Assertions.assertThat(listResponse).hasSize(6);
    }


}
