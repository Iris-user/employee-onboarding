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
    void getAll_returnsAllEmployeesFromRepository() {
        when(repository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = service.getAll();

        assertThat(result).containsExactly(employee);
    }

    @Test
    void getById_returnsEmployeeWhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = service.getById(1L);

        assertThat(result).isEqualTo(employee);
    }

    @Test
    void getById_returnsNullWhenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee result = service.getById(99L);

        assertThat(result).isNull();
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
