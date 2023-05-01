package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> getEmployee(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Page<Employee> getEmployeeByQuery(String query, Pageable pageable) {
        return employeeRepository.findEmployeesByQuery(query, pageable);
    }

    public Page<Employee> getEmployeeByPosition(String position, Pageable pageable) {
        try {
            Position positionConverted = Position.valueOf(position);
            return employeeRepository.findByPosition(positionConverted, pageable);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BusinessRuleException("Função inválida.");
        }
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return Optional.ofNullable(employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Funcionário não encontrado. ")));
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new BusinessRuleException("Já existe um funcionário cadastrado com o CPF("+employee.getCpf()+")");
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new BusinessRuleException("O id do funcionário que está tentando modificar não é válido.");
        }

        Optional<Employee> employeeTest = employeeRepository.findByCpf(employee.getCpf());
        if (!employeeTest.isEmpty() && employeeTest.get().getId() != employee.getId()) {
            throw new BusinessRuleException("Já existe um funcionário cadastrado com o CPF("+employee.getCpf()+")");
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("Funcionário não encontrado. ");
        }
        employeeRepository.deleteById(id);
    }
}
