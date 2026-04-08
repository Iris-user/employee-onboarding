package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService service;

    @Test
    void shouldCreateEmployeeWithLastName() throws Exception {
        Employee input = new Employee(null, "Jane", "Doe", "jane@example.com", "HR");
        Employee saved = new Employee(1L, "Jane", "Doe", "jane@example.com", "HR");
        when(service.save(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.name").value("Jane"));
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        when(service.getAll()).thenReturn(List.of(
                new Employee(1L, "Jane", "Doe", "jane@example.com", "HR")
        ));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        Employee input = new Employee(null, "Jane", "Smith", "jane@example.com", "HR");
        Employee updated = new Employee(1L, "Jane", "Smith", "jane@example.com", "HR");
        when(service.update(eq(1L), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }
}
