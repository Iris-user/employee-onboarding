package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @Test
    void shouldCreateEmployeeWithLastName() throws Exception {
        Employee emp = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering");
        when(service.save(any(Employee.class))).thenReturn(emp);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"department\":\"Engineering\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        Employee emp = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering");
        when(service.getAll()).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void shouldUpdateLastName() throws Exception {
        Employee updated = new Employee(1L, "John", "Smith", "john.doe@example.com", "Engineering");
        when(service.updateLastName(1L, "Smith")).thenReturn(updated);

        mockMvc.perform(patch("/employees/1/last-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }
}
