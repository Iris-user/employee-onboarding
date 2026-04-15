package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void shouldSaveEmployeeWithLastName() {
        when(repository.save(any(Employee.class))).thenReturn(employee);

        Employee saved = service.save(employee);

        assertThat(saved.getLastName()).isEqualTo("Doe");
        verify(repository, times(1)).save(employee);
    }

    @Test
    void shouldUpdateLastName() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee updated = service.updateLastName(1L, "Smith");

        assertThat(updated.getLastName()).isEqualTo("Smith");
        verify(repository).findById(1L);
        verify(repository).save(employee);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFoundForUpdate() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateLastName(99L, "Smith"));
    }
}
