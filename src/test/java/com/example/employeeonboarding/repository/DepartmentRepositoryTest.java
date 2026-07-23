package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;

    @Test
    void findByName_returnsDepartmentWhenNameExists() {
        repository.save(new Department(null, "Engineering"));

        Optional<Department> result = repository.findByName("Engineering");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Engineering");
    }

    @Test
    void findByName_returnsEmptyWhenNameDoesNotExist() {
        Optional<Department> result = repository.findByName("Marketing");

        assertThat(result).isEmpty();
    }
}
