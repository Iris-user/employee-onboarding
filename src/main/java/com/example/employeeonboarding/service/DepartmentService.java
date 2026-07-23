package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.DepartmentNotFoundException;
import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public Department save(Department department) {
        return repository.save(department);
    }

    public List<Department> getAll() {
        return repository.findAll();
    }

    public Department getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
    }
}
