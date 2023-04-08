package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.EmployeeDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.service.AddressService;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.getEmployee(pageable);
        return ResponseEntity.ok(employeePage.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        int startItem = size * page;
        List<Employee> pageList;
        List<Employee> employees = employeeService.getEmployeeByQuery(query);

        if(employees.size() < startItem){
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + size, employees.size());
            pageList = employees.subList(startItem, toIndex);
        }

        return ResponseEntity.ok(pageList.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Employee employeeResponse = employeeService.getEmployeeById(id);
            EmployeeDTO employeeDTO = EmployeeDTO.create(employeeResponse);
            return ResponseEntity.ok(employeeDTO);
        } catch(BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee employee = converter(employeeDTO);
            Address address = addressService.saveAddress(employee.getAddress());
            employee.setAddress(address);
            employeeService.saveEmployee(employee);
            return new ResponseEntity("Funcionário armazenado com sucesso!", HttpStatus.CREATED);
        } catch(BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee employee = converter(employeeDTO);
            employee.setId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok("Funcionário atualizado com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Funcionário excluído com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Employee converter(EmployeeDTO employeeDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);

        employee.setBornDate(LocalDate.parse(employeeDTO.getBornDate()));

        Address address = modelMapper.map(employeeDTO, Address.class);
        employee.setAddress(address);
        return employee;
    }


}
