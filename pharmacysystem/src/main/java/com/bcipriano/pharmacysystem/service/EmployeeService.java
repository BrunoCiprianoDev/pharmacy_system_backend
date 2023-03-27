package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Profile;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee authentication(String email, String password);

    Employee saveEmployee(Employee employee);

    void validateEmployeeByEmail(String email);

    void validatePassword(String password);

    Optional<Employee> getEmployeeById(Long id);

    List<Employee> getEmployees();

    List<Employee> getEmployeesByProfile(Profile profile);

}
