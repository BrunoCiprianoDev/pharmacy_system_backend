package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public static void validateEmployee(Employee employee) {
        String cpfPattern = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
        String phonePattern = "^\\(\\d{2}\\)\\d{4}\\-\\d{4}$";
        String cellPhonePattern = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        LocalDate now = LocalDate.now();
        LocalDate minBirthDate = now.minusYears(120);
        LocalDate maxBirthDate = now.minusYears(18);

        if (employee.getName() == null || employee.getName().trim().equals("")) {
            throw new BusinessRuleException("O campo nome do funcionário é obrigatório.");
        }
        if (employee.getCpf() == null) {
            throw new BusinessRuleException("O campo CPF do funcionário é obrigatório.");
        }
        if (!employee.getCpf().matches(cpfPattern)) {
            throw new BusinessRuleException("CPF do funcionário inválido.");
        }
        if (employee.getBornDate() == null) {
            throw new BusinessRuleException("O campo data de nascimento do funcionário é obrigatório.");
        }
        if (employee.getBornDate().isBefore(minBirthDate) || employee.getBornDate().isAfter(maxBirthDate)) {
            throw new BusinessRuleException("Data de nascimento do funcionário inválida.");
        }
        if (employee.getAddress() == null) {
            throw new BusinessRuleException("Endereço do funcionário inválido.");
        }

        AddressService.validateAddress(employee.getAddress());

        if (employee.getPrimaryPhone() == null) {
            throw new BusinessRuleException("O campo telefone 1 do funcionário é obrigatório.");
        }
        if (!employee.getPrimaryPhone().matches(phonePattern) && !employee.getPrimaryPhone().matches(cellPhonePattern)) {
            throw new BusinessRuleException("Telefone 1 do funcionário é inválido.");
        }
        if (employee.getSecundaryPhone() != null) {
            if (!employee.getSecundaryPhone().matches(phonePattern) && !employee.getSecundaryPhone().matches(cellPhonePattern)) {
                throw new BusinessRuleException("Telefone 2 do funcionário é inválido.");
            }
        }
        if (employee.getEmail() == null) {
            throw new BusinessRuleException("O campo e-mail do funcionário é obrigatório.");
        }
        if (!employee.getEmail().matches(emailPattern)) {
            throw new BusinessRuleException("E-mail do funcionário é inválido.");
        }
        if (employee.getPosition() == null) {
            throw new BusinessRuleException("Selecione uma função para o funcionário");
        }

    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new BusinessRuleException("Ja existe um funcionario cadastrado usando esse cpf.");
        }
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new BusinessRuleException("O id do funcionário que está tentando modificar não é válido.");
        }

        Optional<Employee> employeeTest = employeeRepository.findByCpf(employee.getCpf());
        if (!employeeTest.isEmpty() && employeeTest.get().getId() != employee.getId()) {
            throw new BusinessRuleException("Erro ao atualizar o funcionário. Já existe outro funcionário com esse CPF.");
        }

        validateEmployee(employee);
        return employeeRepository.save(employee);
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
            throw new IllegalArgumentException("Valor inválido para função do funcionário.");
        }
    }

    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new NotFoundException("O id do funcionário que está tentando buscar não é válido.");
        }
        return employee;
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("O id do funcionário que está tentando excluir não é válido.");
        }
        employeeRepository.deleteById(id);
    }
}
