package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.DepartmentNotFoundException;
import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.repository.DepartmentRepository;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;

    @Mock
    private EmployeeRepository employeeRepository;

    private DepartmentService service;

    private Department department;

    @BeforeEach
    void setUp() {
        service = new DepartmentService(repository, employeeRepository);
        department = new Department(1L, "Engineering");
    }

    @Test
    void shouldSaveDepartment() {
        when(repository.save(any(Department.class))).thenReturn(department);

        Department saved = service.save(department);

        assertThat(saved.getName()).isEqualTo("Engineering");
    }

    @Test
    void getAll_returnsAllDepartmentsFromRepository() {
        when(repository.findAll()).thenReturn(List.of(department));

        List<Department> result = service.getAll();

        assertThat(result).containsExactly(department);
    }

    @Test
    void getById_returnsDepartmentWhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));

        Department result = service.getById(1L);

        assertThat(result).isEqualTo(department);
    }

    @Test
    void getById_throwsDepartmentNotFoundException_whenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    void getEmployeeCount_returnsCountFromRepository_whenDepartmentExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.countByDepartmentId(1L)).thenReturn(3L);

        long count = service.getEmployeeCount(1L);

        assertThat(count).isEqualTo(3L);
    }

    @Test
    void getEmployeeCount_throwsDepartmentNotFoundException_whenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEmployeeCount(99L))
                .isInstanceOf(DepartmentNotFoundException.class);
    }
}
