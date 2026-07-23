package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    public Department createDepartment(@Valid @RequestBody Department department) {
        return service.save(department);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return service.getById(id);
    }
}
