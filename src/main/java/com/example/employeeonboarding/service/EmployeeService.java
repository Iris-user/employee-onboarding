package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public Employee getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Employee update(Long id, Employee updatedEmployee) {
        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if (updatedEmployee.getLastName() != null
                && !Objects.equals(updatedEmployee.getLastName(), existingEmployee.getLastName())) {
            throw new LastNameUpdateNotAllowedException("Last name cannot be updated");
        }

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());

        return repository.save(existingEmployee);
    }
}