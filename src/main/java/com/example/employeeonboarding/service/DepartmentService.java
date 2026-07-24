package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.DepartmentNotFoundException;
import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.repository.DepartmentRepository;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
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

    public long getEmployeeCount(Long id) {
        getById(id);
        return employeeRepository.countByDepartmentId(id);
    }
}
