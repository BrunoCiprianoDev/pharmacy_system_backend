package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    void validateEmployee(Employee employee);

    Employee saveEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    Page<Employee> getEmployee(Pageable pageable);

    Employee getEmployeeById(Long id);

    Employee findByEmail(String email);

    List<Employee> getEmployeeByQuery(String query);

    void deleteEmployee(Long id);

}
