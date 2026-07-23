package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.exception.DepartmentNotFoundException;
import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService service;

    @Test
    void shouldCreateDepartment() throws Exception {
        Department department = new Department(1L, "Engineering");
        when(service.save(any(Department.class))).thenReturn(department);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Engineering\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void createDepartment_returnsBadRequest_whenNameMissing() throws Exception {
        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllDepartments() throws Exception {
        Department department = new Department(1L, "Engineering");
        when(service.getAll()).thenReturn(List.of(department));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Engineering"));
    }

    @Test
    void getDepartmentById_returnsDepartmentWhenIdExists() throws Exception {
        Department department = new Department(1L, "Engineering");
        when(service.getById(1L)).thenReturn(department);

        mockMvc.perform(get("/departments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void getDepartmentById_returnsNotFound_whenIdDoesNotExist() throws Exception {
        when(service.getById(99L)).thenThrow(new DepartmentNotFoundException("Department not found with id: 99"));

        mockMvc.perform(get("/departments/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
