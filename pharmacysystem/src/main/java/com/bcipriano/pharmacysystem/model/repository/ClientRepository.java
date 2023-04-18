package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCpf(String cpf);

    Optional<Client> findClientById(Long id);

    Optional<Client> findByCpf(String cpf);

    @Query(value = "SELECT c FROM Client c WHERE " +
            "c.email LIKE %:query% " +
            "OR (c.name LIKE %:query%) " +
            "OR (c.cpf LIKE %:query%) " +
            "OR (c.phone LIKE %:query%)")
    Page<Client> findClientsByQuery(@Param("query") String query, Pageable pageable);
}