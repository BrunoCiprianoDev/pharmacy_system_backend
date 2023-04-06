package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.service.AddressService;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void validateEmployee(Employee employee) {

        String cpfPattern = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
        String phonePattern = "^\\(\\d{2}\\)\\d{4}\\-\\d{4}$";
        String cellPhonePattern = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordPattern = "^(.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]){2}.*$";

        LocalDate now = LocalDate.now();
        LocalDate minBirthDate = now.minusYears(120);
        LocalDate maxBirthDate = now.minusYears(18);

        if(employee.getName() == null || employee.getName().trim().equals("")){
            throw new BusinessRuleException("O campo nome é obrigatório.");
        }
        if(employee.getCpf() == null){
            throw new BusinessRuleException("O campo CPF é obrigatório.");
        }
        if(!employee.getCpf().matches(cpfPattern)){
            throw new BusinessRuleException("CPF inválido.");
        }
        if(employee.getBornDate() == null){
            throw new BusinessRuleException("O campo data de nascimento é obrigatório.");
        }
        if(employee.getBornDate().isBefore(minBirthDate) || employee.getBornDate().isAfter(maxBirthDate)){
            throw new BusinessRuleException("Data de nascimento inválida.");
        }
        if(employee.getAddress() == null){
            throw new BusinessRuleException("Endereço inválido.");
        }

        AddressService.validateAddress(employee.getAddress());

        if(employee.getPrimaryPhone() == null){
            throw new BusinessRuleException("O campo telefone 1 é obrigatório.");
        }
        if(!employee.getPrimaryPhone().matches(phonePattern) && !employee.getPrimaryPhone().matches(cellPhonePattern)){
            throw new BusinessRuleException("Telefone 1 inválido.");
        }
        if(employee.getSecundaryPhone() != null) {
            if(!employee.getSecundaryPhone().matches(phonePattern) && !employee.getSecundaryPhone().matches(cellPhonePattern)){
                throw new BusinessRuleException("Telefone 2 inválido.");
            }
        }
        if(employee.getEmail() == null){
            throw new BusinessRuleException("O campo e-mail é obrigatório.");
        }
        if(!employee.getEmail().matches(emailPattern)){
            throw new BusinessRuleException("E-mail inválido.");
        }
        if(employee.getPassword() == null){
            throw new BusinessRuleException("A senha não foi preenchida.");
        }

        if(employee.getPassword().length() < 6){
            throw new BusinessRuleException("A senha de conter, no mínimo, 6 caracteres.");
        }
        if(!employee.getPassword().matches(passwordPattern)){
            throw new BusinessRuleException("A senha deve conter pelo menos 2 caracteres especiais e 1 letra maiúscula.");
        }
        if(employee.getProfile() == null){
            throw new BusinessRuleException("Selecione um perfil para o funcionário");
        }
    }

    @Override
    @Transactional
    public Employee saveEmployee(Employee employee) {
        validateEmployee(employee);
        if(employeeRepository.existsByCpf(employee.getCpf())){
            throw new BusinessRuleException("Ja existe um funcionario cadastrado usando esse cpf.");
        }
        if(employeeRepository.existsByEmail(employee.getEmail())){
            throw new BusinessRuleException("Ja existe um funcionario cadastrado usando esse email.");
        }
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) {
        if(!employeeRepository.existsById(employee.getId())) {
            throw new BusinessRuleException("O funcionário que está tentado modificar não está cadastrado.");
        }
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            throw new BusinessRuleException("Funcionario com id inválido.");
        }
        return employee.get();
    }

    @Override
    public Employee findByEmail(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if(employee.isEmpty()){
            throw new BusinessRuleException("E-mail inválido para esse funcionário.");
        }
        return employee.get();
    }

    @Override
    public List<Employee> getEmployeeByQuery(String query) {
        return employeeRepository.findEmployeesByQuery(query);
    }

    @Override
    public void deleteEmployee(Long id) {
        boolean exists = employeeRepository.existsById(id);
        if(!exists){
            throw new BusinessRuleException("Funcionario com id inválido.");
        }
        employeeRepository.deleteById(id);
    }
}
