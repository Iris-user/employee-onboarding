package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void findByDepartmentName_returnsOnlyEmployeesInThatDepartment() {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Department sales = departmentRepository.save(new Department(null, "Sales"));

        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", engineering, 27, null, null, null));
        repository.save(new Employee(null, "Bob", "Jones", "bob@example.com", sales, 30, null, null, null));
        repository.save(new Employee(null, "Carol", "White", "carol@example.com", engineering, 35, null, null, null));

        List<Employee> result = repository.findByDepartmentName("Engineering");

        assertThat(result).extracting(Employee::getName).containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    void findByDepartmentName_returnsEmptyListWhenNoEmployeesMatch() {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", engineering, 27, null, null, null));

        List<Employee> result = repository.findByDepartmentName("Marketing");

        assertThat(result).isEmpty();
    }

    @Test
    void findByAge_returnsOnlyEmployeesWithThatAge() {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Department sales = departmentRepository.save(new Department(null, "Sales"));

        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", engineering, 30, null, null, null));
        repository.save(new Employee(null, "Bob", "Jones", "bob@example.com", sales, 40, null, null, null));
        repository.save(new Employee(null, "Carol", "White", "carol@example.com", engineering, 30, null, null, null));

        List<Employee> result = repository.findByAge(30);

        assertThat(result).extracting(Employee::getName).containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    void findByAge_returnsEmptyListWhenNoEmployeesMatch() {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", engineering, 30, null, null, null));

        List<Employee> result = repository.findByAge(99);

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_returnsOnlyEmployeesWithThatEmail() {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Department sales = departmentRepository.save(new Department(null, "Sales"));

        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", engineering, 30, null, null, null));
        repository.save(new Employee(null, "Bob", "Jones", "bob@example.com", sales, 40, null, null, null));

        List<Employee> result = repository.findByEmail("alice@example.com");

        assertThat(result).extracting(Employee::getName).containsExactly("Alice");
    }

    @Test
    void findByEmail_returnsEmptyListWhenNoEmployeesMatch() {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", engineering, 30, null, null, null));

        List<Employee> result = repository.findByEmail("nobody@example.com");

        assertThat(result).isEmpty();
    }
}
