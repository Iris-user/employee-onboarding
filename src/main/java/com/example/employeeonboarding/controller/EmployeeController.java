package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee emp) {
        return service.save(emp);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee emp) {
        return service.update(id, emp)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
