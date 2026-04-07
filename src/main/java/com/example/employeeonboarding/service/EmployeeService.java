package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee save(Employee emp) {
        return repository.save(emp);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Optional<Employee> update(Long id, Employee updatedData) {
        return repository.findById(id).map(existing -> {
            existing.setFirstName(updatedData.getFirstName());
            existing.setLastName(updatedData.getLastName());
            existing.setEmail(updatedData.getEmail());
            existing.setDepartment(updatedData.getDepartment());
            return repository.save(existing);
        });
    }
}
