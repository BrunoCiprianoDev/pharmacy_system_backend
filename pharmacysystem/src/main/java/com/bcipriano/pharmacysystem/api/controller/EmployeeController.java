package com.bcipriano.pharmacysystem.api.controller;



import com.bcipriano.pharmacysystem.api.dto.EmployeeDTO;
//import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

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
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.getEmployeeByQuery(query, pageable);
        return ResponseEntity.ok(employeePage.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/position")
    public ResponseEntity getByPosition(@RequestParam("position") String position,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employee> employeePage = employeeService.getEmployeeByPosition(position, pageable);
            return ResponseEntity.ok(employeePage.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Optional<Employee> employeeResponse = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employeeResponse.map(EmployeeDTO::create));
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody @Valid EmployeeDTO employeeDTO) {
        try {
            Employee employee = converter(employeeDTO);
            employeeService.saveEmployee(employee);
            return new ResponseEntity("Funcionário armazenado com sucesso!", HttpStatus.CREATED);
        } catch (RuntimeException runtimeException){
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee employee = converter(employeeDTO);
            employee.setId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok("Funcionário atualizado com sucesso!");
        } catch (RuntimeException runtimeException){
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Funcionário excluído com sucesso!");
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    public static Employee converter(EmployeeDTO employeeDTO) {

        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);

        employee.setBornDate(LocalDate.parse(employeeDTO.getBornDate()));

        Address address = modelMapper.map(employeeDTO, Address.class);
        employee.setAddress(address);

        employee.setPosition(Position.fromString(employeeDTO.getPosition()));

        return employee;

    }

}
