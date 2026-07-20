package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void createEmployee_returnsCreatedEmployee() throws Exception {
        Employee toCreate = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        Employee created = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.save(toCreate)).thenReturn(created);

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    void getAllEmployees_returnsListOfEmployees() throws Exception {
        Employee employee = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.getAll()).thenReturn(List.of(employee));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice Smith"));
    }

    @Test
    void getEmployeeById_returnsEmployeeWhenIdExists() throws Exception {
        Employee employee = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.getById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    void getEmployeeById_returns404WhenIdDoesNotExist() throws Exception {
        when(service.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/employees/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
