package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReturnRepository extends JpaRepository<Return, Long> {

    Optional<Return> findBySaleItemId(Long id);

}