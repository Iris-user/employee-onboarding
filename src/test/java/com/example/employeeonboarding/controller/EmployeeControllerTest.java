package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEmployee_shouldIncludeLastName() throws Exception {
        Employee input = new Employee(null, "John", "Doe", "john.doe@example.com", "Engineering");
        Employee saved = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering");

        when(service.save(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"));
    }

    @Test
    void getAllEmployees_shouldReturnList() throws Exception {
        Employee emp = new Employee(1L, "Jane", "Smith", "jane.smith@example.com", "HR");
        when(service.getAll()).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }

    @Test
    void updateEmployee_shouldUpdateLastName() throws Exception {
        Employee updated = new Employee(1L, "John", "UpdatedDoe", "john.doe@example.com", "Engineering");

        when(service.update(eq(1L), any(Employee.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("UpdatedDoe"));
    }

    @Test
    void updateEmployee_shouldReturn404WhenNotFound() throws Exception {
        Employee updated = new Employee(null, "John", "UpdatedDoe", "john.doe@example.com", "Engineering");

        when(service.update(eq(99L), any(Employee.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/employees/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }
}
