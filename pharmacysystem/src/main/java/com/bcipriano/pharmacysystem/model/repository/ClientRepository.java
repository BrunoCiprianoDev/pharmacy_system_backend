package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCpf(String cpf);

    @Query(value = "SELECT c FROM Client c WHERE " +
            "c.email LIKE %:query% " +
            "OR (c.name LIKE %:query%) " +
            "OR (c.cpf LIKE %:query%) " +
            "OR (c.phone LIKE %:query%)")
    List<Client> findClientsByQuery(@Param("query") String query);

}
