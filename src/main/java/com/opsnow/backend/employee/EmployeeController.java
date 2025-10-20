package com.opsnow.backend.employee;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.opsnow.backend.employee.dtos.EmployeeAverageSalaryDto;
import com.opsnow.backend.employee.dtos.EmployeeCreateDto;
import com.opsnow.backend.employee.dtos.EmployeeCumulativeSalaryDto;
import com.opsnow.backend.employee.dtos.EmployeeDto;
import com.opsnow.backend.employee.dtos.EmployeeSalaryGapDto;
import com.opsnow.backend.employee.dtos.EmployeeUpdateDto;

import com.opsnow.backend.utils.ApiException;
import com.opsnow.backend.utils.PaginatedResponse;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeCreateDto request)
            throws ApiException {
        EmployeeDto response = employeeService.createEmployee(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{empNo}")
    public ResponseEntity<Object> updateEmployee(
            @PathVariable Integer empNo,
            @Valid @RequestBody EmployeeUpdateDto request)
            throws ApiException {
        EmployeeDto response = employeeService.updateEmployee(empNo, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{empNo}")
    public ResponseEntity<Object> getEmployeeByEmpNo(@PathVariable Integer empNo) throws ApiException {
        EmployeeDto response = employeeService.getEmployeeByEmpNo(empNo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{empNo}")
    public ResponseEntity<Object> deleteEmployeeByEmpNo(@PathVariable Integer empNo)
            throws ApiException {
        EmployeeDto response = employeeService.deleteEmployeeByEmpNo(empNo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllEmployees(
            @RequestParam(required = false) Integer empNo,
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) Integer supervisorCode,
            @RequestParam(required = false) Integer tierCode,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(required = false) String locationCode,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) throws ApiException {
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        Pageable paginationQueries = PageRequest.of(page, pageSize);
        Specification<Employee> filterQueries = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (empName != null && !empName.isEmpty())
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("empName")),
                                "%" + empName.toLowerCase() + "%")));

            if (empNo != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("empNo"), empNo));

            if (supervisorCode != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("supervisorCode"), supervisorCode));

            if (departmentCode != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("department").get("departmentCode"), departmentCode));

            if (locationCode != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("location").get("locationCode"), locationCode));

            if (tierCode != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("tier").get("tierCode"), tierCode));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        PaginatedResponse<EmployeeDto> res = new PaginatedResponse<>(
                employeeService.getAllEmployees(filterQueries, paginationQueries));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/cumulative-salary")
    public ResponseEntity<Object> getEmployeeCumulativeSalary() throws ApiException {
        List<EmployeeCumulativeSalaryDto> response = employeeService.getEmployeeCumulativeSalary();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/department-analysis")
    public ResponseEntity<Object> getAverageSalaryByLocationForLowestSalaryDepartment() throws ApiException {
        List<EmployeeAverageSalaryDto> response = employeeService.getAverageSalaryByLocationForLowestSalaryDepartment();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/salary-gap")
    public ResponseEntity<Object> getSalaryGap() throws ApiException {
        List<EmployeeSalaryGapDto> response = employeeService.getSalaryGap();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
