package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    private EmployeeService service;

    private Employee existingEmployee;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        service = new EmployeeService(repository);
        existingEmployee = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering");
    }

    @Test
    void save_persistsLastNameAlongWithOtherAttributes() {
        Employee toSave = new Employee(null, "Jane", "Smith", "jane.smith@example.com", "HR");
        Employee saved = new Employee(2L, "Jane", "Smith", "jane.smith@example.com", "HR");
        when(repository.save(toSave)).thenReturn(saved);

        Employee result = service.save(toSave);

        assertThat(result.getLastName()).isEqualTo("Smith");
        verify(repository).save(toSave);
    }

    @Test
    void getAll_returnsAllEmployeesFromRepository() {
        Employee employee = new Employee(1L, "Alice", "Smith", "alice@example.com", "Engineering");
        when(repository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = service.getAll();

        assertThat(result).containsExactly(employee);
    }

    @Test
    void getById_returnsEmployeeWhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(existingEmployee));

        Employee result = service.getById(1L);

        assertThat(result).isEqualTo(existingEmployee);
    }

    @Test
    void getById_returnsNullWhenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee result = service.getById(99L);

        assertThat(result).isNull();
    }

    @Test
    void update_appliesChangesToNameEmailAndDepartment_whenLastNameUnchanged() {
        when(repository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee update = new Employee(null, "Johnny", "Doe", "johnny.doe@example.com", "Sales");

        Employee result = service.update(1L, update);

        assertThat(result.getName()).isEqualTo("Johnny");
        assertThat(result.getEmail()).isEqualTo("johnny.doe@example.com");
        assertThat(result.getDepartment()).isEqualTo("Sales");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void update_leavesLastNameUnchanged_whenRequestOmitsLastName() {
        when(repository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee update = new Employee(null, "Johnny", null, "johnny.doe@example.com", "Sales");

        Employee result = service.update(1L, update);

        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void update_throwsLastNameUpdateNotAllowedException_whenLastNameIsChanged() {
        when(repository.findById(1L)).thenReturn(Optional.of(existingEmployee));

        Employee update = new Employee(null, "John", "Doeson", "john.doe@example.com", "Engineering");

        assertThatThrownBy(() -> service.update(1L, update))
                .isInstanceOf(LastNameUpdateNotAllowedException.class);

        verify(repository, org.mockito.Mockito.never()).save(any(Employee.class));
    }

    @Test
    void update_throwsEmployeeNotFoundException_whenEmployeeDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee update = new Employee(null, "Ghost", "Employee", "ghost@example.com", "Ops");

        assertThatThrownBy(() -> service.update(99L, update))
                .isInstanceOf(EmployeeNotFoundException.class);
    }
}
