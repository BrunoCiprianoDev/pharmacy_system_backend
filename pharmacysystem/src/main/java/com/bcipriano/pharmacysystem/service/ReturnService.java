package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Return;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReturnService {

    void validateReturn(Return rtn);

    Return saveReturn(Return rtn);

    Return updateReturn(Return rtn);

    Page<Return> getReturn(Pageable pageable);

    Return getReturnBySaleItemId(Long saleId);

    Return getReturnById(Long id);

    void deleteReturn(Long id);

}
