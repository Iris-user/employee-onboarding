package com.example.employeeonboarding;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import com.example.employeeonboarding.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveEmployeeWithLastName() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"lastName\":\"Johnson\",\"email\":\"alice@example.com\",\"department\":\"HR\",\"age\":28}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Johnson"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingEmployeeWithMissingFields() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"lastName\":\"Johnson\"}"))
                .andExpect(status().isBadRequest());

        assertThat(employeeRepository.findAll()).isEmpty();
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        employeeService.save(new Employee(null, "Bob", "Brown", "bob@example.com", "Finance", 30));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Brown"));
    }

    @Test
    void shouldUpdateLastNameViaEndpoint() throws Exception {
        Employee saved = employeeService.save(new Employee(null, "Carol", "White", "carol@example.com", "IT", 30));

        mockMvc.perform(patch("/employees/" + saved.getId() + "/last-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Black\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Black"));
    }

    @Test
    void shouldUpdateLastNameViaService() {
        Employee saved = employeeService.save(new Employee(null, "Dan", "Green", "dan@example.com", "Legal", 30));

        Employee updated = employeeService.updateLastName(saved.getId(), "Grey");

        assertThat(updated.getLastName()).isEqualTo("Grey");
    }

    @Test
    void shouldThrowWhenUpdatingLastNameForNonExistentEmployee() {
        assertThrows(RuntimeException.class, () -> employeeService.updateLastName(9999L, "Nobody"));
    }

    @Test
    void shouldDeleteEmployeeViaEndpoint() throws Exception {
        Employee saved = employeeService.save(new Employee(null, "Eve", "Black", "eve@example.com", "Support", 30));

        mockMvc.perform(delete("/employees/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentEmployee() throws Exception {
        mockMvc.perform(delete("/employees/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMultipleEmployeesViaEndpoint() throws Exception {
        Employee first = employeeService.save(new Employee(null, "Frank", "Miller", "frank@example.com", "Sales", 40));
        Employee second = employeeService.save(new Employee(null, "Grace", "Hopper", "grace@example.com", "Engineering", 45));

        mockMvc.perform(delete("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ids\":[" + first.getId() + "," + second.getId() + "]}"))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findById(first.getId())).isEmpty();
        assertThat(employeeRepository.findById(second.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMultipleEmployeesWithUnknownId() throws Exception {
        Employee saved = employeeService.save(new Employee(null, "Henry", "Ford", "henry@example.com", "Engineering", 50));

        mockMvc.perform(delete("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ids\":[" + saved.getId() + ",9999]}"))
                .andExpect(status().isNotFound());

        assertThat(employeeRepository.findById(saved.getId())).isPresent();
    }
}
