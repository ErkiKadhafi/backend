package com.opsnow.backend.department;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.opsnow.backend.department.dtos.DepartmentDto;
import com.opsnow.backend.department.dtos.UpdateDepartmentDto;
import com.opsnow.backend.utils.ApiException;
import com.opsnow.backend.utils.PaginatedResponse;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody DepartmentDto request)
            throws ApiException {
        DepartmentDto response = departmentService.createDepartment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{departmentCode}")
    public ResponseEntity<Object> updateDepartment(
            @PathVariable String departmentCode,
            @Valid @RequestBody UpdateDepartmentDto request)
            throws ApiException {
        DepartmentDto response = departmentService.updateDepartment(departmentCode, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{departmentCode}")
    public ResponseEntity<Object> getDepartmentByCode(@PathVariable String departmentCode) throws ApiException {
        DepartmentDto response = departmentService.getDepartmentByCode(departmentCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{departmentCode}")
    public ResponseEntity<Object> deleteDepartmentByCode(@PathVariable String departmentCode)
            throws ApiException {
        DepartmentDto response = departmentService.deleteDepartmentByCode(departmentCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllDepartments(
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) throws ApiException {
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        Pageable paginationQueries = PageRequest.of(page, pageSize);
        Specification<Department> filterQueries = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (departmentName != null && !departmentName.isEmpty())
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("departmentName")),
                                "%" + departmentName.toLowerCase() + "%")));

            if (departmentCode != null && !departmentCode.isEmpty())
                predicates.add(
                        criteriaBuilder.equal(root.get("departmentCode"), departmentCode));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        PaginatedResponse<DepartmentDto> res = new PaginatedResponse<>(
                departmentService.getAllDepartments(filterQueries, paginationQueries));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
