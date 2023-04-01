package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;

import java.time.LocalDate;
import java.util.List;

public interface DiscountGroupService {

    void validateDiscountGroup(DiscountGroup discountGroup);

    DiscountGroup saveDiscountGroup(DiscountGroup discountGroup);

    DiscountGroup updateDiscountGroup(DiscountGroup discountGroup);

    List<DiscountGroup> getDiscountGroup();

    DiscountGroup getDiscountGroupById(Long id);

    List<DiscountGroup> getDiscountGroupById(String query);

    List<DiscountGroup> getDiscountGroupByQuery(String query);

    List<DiscountGroup> getDiscountGroupByStartDate(LocalDate startDate);

    List<DiscountGroup> getDiscountGroupByFinalDate(LocalDate finalDate);

    void deleteDiscountGroup(Long id);

}
