package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering");
    }

    @Test
    void save_shouldPersistEmployeeWithLastName() {
        when(repository.save(any(Employee.class))).thenReturn(employee);

        Employee result = service.save(employee);

        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        verify(repository).save(employee);
    }

    @Test
    void getAll_shouldReturnAllEmployees() {
        when(repository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");
    }

    @Test
    void update_shouldUpdateEmployeeFields() {
        Employee updatedData = new Employee(null, "John", "NewDoe", "john.new@example.com", "Finance");
        Employee savedEmployee = new Employee(1L, "John", "NewDoe", "john.new@example.com", "Finance");

        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenReturn(savedEmployee);

        Optional<Employee> result = service.update(1L, updatedData);

        assertThat(result).isPresent();
        assertThat(result.get().getLastName()).isEqualTo("NewDoe");
        assertThat(result.get().getEmail()).isEqualTo("john.new@example.com");
        assertThat(result.get().getDepartment()).isEqualTo("Finance");
    }

    @Test
    void update_shouldReturnEmptyWhenEmployeeNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = service.update(99L, employee);

        assertThat(result).isEmpty();
        verify(repository, never()).save(any());
    }
}
