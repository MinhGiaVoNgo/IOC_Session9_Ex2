package com.example.hrm.controller;

import com.example.hrm.dto.ApiResponse;
import com.example.hrm.dto.DepartmentDTO;
import com.example.hrm.entity.Department;
import com.example.hrm.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Department>> createDepartment(@Valid @RequestBody DepartmentDTO dto) {
        Department created = departmentService.createDepartment(dto);
        ApiResponse<Department> response = ApiResponse.<Department>builder()
                .status("SUCCESS")
                .message("Tạo phòng ban thành công")
                .data(created)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
