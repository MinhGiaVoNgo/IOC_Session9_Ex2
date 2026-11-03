package com.example.hrm.controller;

import com.example.hrm.dto.DepartmentDTO;
import com.example.hrm.entity.Department;
import com.example.hrm.exception.GlobalExceptionHandler;
import com.example.hrm.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@Import(GlobalExceptionHandler.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    @Test
    @DisplayName("POST /api/v1/departments - Invalid name returns 400 with standard FAIL response")
    void testCreateDepartment_InvalidName_ReturnsStandardFailResponse() throws Exception {
        DepartmentDTO dto = DepartmentDTO.builder()
                .name("IT")
                .description("Phòng công nghệ")
                .build();

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.message").value("Dữ liệu không hợp lệ"))
                .andExpect(jsonPath("$.data.name").value("Tên phòng ban phải từ 5-50 ký tự"));
    }

    @Test
    @DisplayName("POST /api/v1/departments - Valid department returns 201 with SUCCESS response")
    void testCreateDepartment_Success() throws Exception {
        DepartmentDTO dto = DepartmentDTO.builder()
                .name("Phòng Kỹ thuật")
                .description("Phụ trách phát triển phần mềm")
                .build();

        Department saved = Department.builder()
                .id(1L)
                .name("Phòng Kỹ thuật")
                .description("Phụ trách phát triển phần mềm")
                .build();

        when(departmentService.createDepartment(any(DepartmentDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Tạo phòng ban thành công"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Phòng Kỹ thuật"));
    }
}
