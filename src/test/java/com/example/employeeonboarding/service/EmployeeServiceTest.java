package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void shouldSaveEmployeeWithLastName() {
        Employee emp = new Employee(null, "John", "Doe", "john@example.com", "Engineering");
        Employee saved = new Employee(1L, "John", "Doe", "john@example.com", "Engineering");
        when(repository.save(any(Employee.class))).thenReturn(saved);

        Employee result = service.save(emp);

        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateEmployeeLastName() {
        Employee existing = new Employee(1L, "John", null, "john@example.com", "Engineering");
        Employee updates = new Employee(null, "John", "Smith", "john@example.com", "Engineering");
        Employee updated = new Employee(1L, "John", "Smith", "john@example.com", "Engineering");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Employee.class))).thenReturn(updated);

        Employee result = service.update(1L, updates);

        assertThat(result.getLastName()).isEqualTo("Smith");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEmployee() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(99L, new Employee()));
    }
}
