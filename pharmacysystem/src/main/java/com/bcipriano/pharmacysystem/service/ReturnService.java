package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Return;

import java.util.List;
import java.util.Optional;

public interface ReturnService {

    void validateReturn(Return rtn);

    Return saveReturn(Return rtn);

    Return updateReturn(Return rtn);

    List<Return> getReturn();

    Return getReturnBySaleItemId(Long saleId);

    Return getReturnById(Long id);

    void deleteReturn(Long id);

}
