package com.example.employeeonboarding;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        employeeRepository.deleteAll();
    }

    @Test
    void createEmployee_withLastName_returnsEmployeeWithLastName() throws Exception {
        Employee emp = new Employee(null, "John", "Doe", "john@example.com", "Engineering");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void updateLastName_updatesEmployeeLastName() throws Exception {
        Employee saved = employeeRepository.save(new Employee(null, "Jane", null, "jane@example.com", "HR"));

        mockMvc.perform(patch("/employees/" + saved.getId() + "/lastName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Smith\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void updateLastName_withNonExistentId_returnsNotFound() throws Exception {
        mockMvc.perform(patch("/employees/9999/lastName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Smith\""))
                .andExpect(status().isNotFound());
    }
}
