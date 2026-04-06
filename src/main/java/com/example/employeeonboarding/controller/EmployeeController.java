package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final ObjectMapper objectMapper;

    public EmployeeController(EmployeeService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee emp) {
        return service.save(emp);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAll();
    }

    @PatchMapping("/{id}/lastName")
    public Employee updateLastName(@PathVariable Long id, @RequestBody String rawBody) throws Exception {
        String lastName = objectMapper.readValue(rawBody, String.class);
        return service.updateLastName(id, lastName);
    }
}
