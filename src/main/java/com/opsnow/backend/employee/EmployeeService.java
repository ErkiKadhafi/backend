package com.opsnow.backend.employee;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opsnow.backend.department.Department;
import com.opsnow.backend.department.DepartmentService;
import com.opsnow.backend.employee.dtos.EmployeeAverageSalaryDto;
import com.opsnow.backend.employee.dtos.EmployeeCreateDto;
import com.opsnow.backend.employee.dtos.EmployeeCumulativeSalaryDto;
import com.opsnow.backend.employee.dtos.EmployeeDto;
import com.opsnow.backend.employee.dtos.EmployeeSalaryGapDto;
import com.opsnow.backend.employee.dtos.EmployeeUpdateDto;
import com.opsnow.backend.location.Location;
import com.opsnow.backend.location.LocationService;
import com.opsnow.backend.tier.Tier;
import com.opsnow.backend.tier.TierService;

import com.opsnow.backend.utils.ApiException;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final DepartmentService departmentService;
    private final TierService tierService;
    private final LocationService locationService;

    public EmployeeService(EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            DepartmentService departmentService,
            TierService tierService,
            LocationService locationService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentService = departmentService;
        this.tierService = tierService;
        this.locationService = locationService;
    }

    public Employee checkIfEmployeeIsExist(Integer empNo) throws ApiException {
        Optional<Employee> employeeOnDb = employeeRepository.findByEmpNo(empNo);
        if (employeeOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "employee with no " + empNo + " is not exist");

        return employeeOnDb.get();
    }

    public EmployeeDto createEmployee(EmployeeCreateDto employeeCreateDto) throws ApiException {
        Optional<Employee> employeeOnDb = employeeRepository
                .findByEmpNo(employeeCreateDto.getEmpNo());

        if (employeeOnDb.isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Employee with no " + employeeCreateDto.getTierCode() + " is already exist");

        departmentService.checkIfDepartmentIsExist(employeeCreateDto.getDepartmentCode());
        tierService.checkIfTierIsExist(employeeCreateDto.getTierCode());
        locationService.checkIfLocationIsExist(employeeCreateDto.getLocationCode());

        if (employeeCreateDto.getSupervisorCode() != null)
            checkIfEmployeeIsExist(employeeCreateDto.getSupervisorCode());

        Employee employee = employeeMapper.employeeCreateDtoToEmployee(employeeCreateDto);
        EmployeeDto res = employeeMapper.toDto(employeeRepository.save(employee));

        return res;
    }

    public EmployeeDto updateEmployee(Integer empNo, EmployeeUpdateDto employeeUpdateDto)
            throws ApiException {
        Employee employee = checkIfEmployeeIsExist(empNo);

        Employee supervisor = null;
        if (employeeUpdateDto.getSupervisorCode() != null)
            supervisor = checkIfEmployeeIsExist(employeeUpdateDto.getSupervisorCode());
        Department department = departmentService.checkIfDepartmentIsExist(employeeUpdateDto.getDepartmentCode());
        Tier tier = tierService.checkIfTierIsExist(employeeUpdateDto.getTierCode());
        Location location = locationService.checkIfLocationIsExist(employeeUpdateDto.getLocationCode());

        employee.setEmpName(employeeUpdateDto.getEmpName());
        employee.setSalary(employeeUpdateDto.getSalary());
        if (supervisor != null)
            employee.setSupervisor(supervisor);
        employee.setDepartment(department);
        employee.setTier(tier);
        employee.setLocation(location);

        EmployeeDto res = employeeMapper.toDto(employeeRepository.saveAndFlush(employee));

        return res;
    }

    public EmployeeDto getEmployeeByEmpNo(Integer empNo) throws ApiException {
        EmployeeDto employee = employeeMapper.toDto(checkIfEmployeeIsExist(empNo));

        return employee;
    }

    @Transactional
    public EmployeeDto deleteEmployeeByEmpNo(Integer empNo)
            throws ApiException {
        checkIfEmployeeIsExist(empNo);

        EmployeeDto res = employeeMapper.toDto(employeeRepository.deleteByEmpNo(empNo));

        return res;
    }

    public Page<EmployeeDto> getAllEmployees(
            Specification<Employee> filterQueries,
            Pageable paginationQueries) {
        Page<Employee> employees = employeeRepository.findAll(filterQueries, paginationQueries);

        return employees.map(t -> employeeMapper.toDto(t));
    }

    public List<EmployeeCumulativeSalaryDto> getEmployeeCumulativeSalary() {
        List<EmployeeCumulativeSalaryDto> res = employeeRepository.getEmployeeCumulativeSalary()
                .stream()
                .map(d -> new EmployeeCumulativeSalaryDto(
                        (String) d.get("departmentcode"),
                        d.get("empno") != null ? ((Number) d.get("empno")).longValue() : null,
                        (String) d.get("empname"),
                        d.get("cumulative_salary") != null ? ((Number) d.get("cumulative_salary")).longValue() : null))
                .collect(Collectors.toList());
        ;
        return res;
    }

    public List<EmployeeAverageSalaryDto> getAverageSalaryByLocationForLowestSalaryDepartment() {
        List<EmployeeAverageSalaryDto> res = employeeRepository.getAverageSalaryByLocationForLowestSalaryDepartment()
                .stream()
                .map(d -> new EmployeeAverageSalaryDto(
                        (String) d.get("location_name"),
                        d.get("employee_dept_count") != null ? ((Number) d.get("employee_dept_count")).longValue()
                                : null,
                        (String) d.get("most_employee_dept"),
                        d.get("avg_salary") != null ? ((Number) d.get("avg_salary")).longValue() : null))
                .collect(Collectors.toList());
        ;
        return res;
    }

    public List<EmployeeSalaryGapDto> getSalaryGap() {
        List<EmployeeSalaryGapDto> res = employeeRepository.getSalaryGap()
                .stream()
                .map(d -> new EmployeeSalaryGapDto(
                        (String) d.get("locationname"),
                        (String) d.get("departmentname"),
                        (String) d.get("empname"),
                        (String) d.get("tiername"),
                        ((Number) d.get("salary")).longValue(),
                        ((Number) d.get("rank")).longValue(),
                        ((Number) d.get("salarygap")).longValue()))
                .collect(Collectors.toList());
        ;
        return res;
    }
}
