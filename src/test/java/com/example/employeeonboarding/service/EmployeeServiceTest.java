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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void update_updatesAndReturnsEmployeeWhenIdExists() {
        Employee existing = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        Employee changes = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        Employee saved = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(saved);

        Employee result = service.update(1L, changes);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void update_returnsNullWhenIdDoesNotExist() {
        Employee changes = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee result = service.update(99L, changes);

        assertThat(result).isNull();
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
