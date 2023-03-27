package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.AuthErrorException;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Profile;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee authentication(String email, String password) {

        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (!employee.isPresent()) {
            throw new AuthErrorException("Funcionário não encontrado!");
        }

        if (!employee.get().getPassword().equals(password)) {
            throw new AuthErrorException("Senha inválida!");
        }

        return employee.get();
    }

    @Override
    @Transactional
    public Employee saveEmployee(Employee employee) {
        validateEmployeeByEmail(employee.getEmail());
        validatePassword(employee.getPassword());
        return employeeRepository.save(employee);
    }

    @Override
    public void validateEmployeeByEmail(String email) {
        boolean exists = employeeRepository.existsByEmail(email);
        if (exists) {
            throw new BusinessRuleException("Já existe um funcionario cadastrado com esse e-mail.");
        }
    }

    @Override
    public void validatePassword(String password) {

        if(password == null) {
            throw new BusinessRuleException("Senha inválida!");
        }

        if (password.length() < 6) {
            throw new BusinessRuleException("A senha deve ter, no mínimo 6 characteres");
        }

        if (!password.matches("^(.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]){2}.*$")) {
            throw new BusinessRuleException("Insira pelo menos 2 caracteres especiais");
        }

        if (!password.matches("^(.*[A-Z]){1}.*$")) {
            throw new BusinessRuleException("A senha deve conter, pelo menos 1 caracter com letra maiúscula");
        }

    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {

        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isEmpty()) {
            throw new NotFoundException("Id inválido!");
        }

        return employee;
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getEmployeesByProfile(Profile profile) {
        return employeeRepository.findByProfile(profile);
    }
}
