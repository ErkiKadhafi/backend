package com.opsnow.backend.department;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opsnow.backend.department.dtos.DepartmentDto;
import com.opsnow.backend.department.dtos.UpdateDepartmentDto;
import com.opsnow.backend.utils.ApiException;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public Department checkIfDepartmentIsExist(String departmentCode) throws ApiException {
        Optional<Department> departmentOnDb = departmentRepository.findByDepartmentCode(departmentCode);
        if (departmentOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Department with code " + departmentCode + " is not exist");

        return departmentOnDb.get();
    }

    public DepartmentDto createDepartment(DepartmentDto departmentDto) throws ApiException {
        Optional<Department> departmentOnDb = departmentRepository
                .findByDepartmentCode(departmentDto.getDepartmentCode());

        if (departmentOnDb.isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Department with code " + departmentDto.getDepartmentCode() + " is already exist");

        Department department = departmentMapper.toEntity(departmentDto);
        DepartmentDto res = departmentMapper.toDto(departmentRepository.save(department));

        return res;
    }

    public DepartmentDto updateDepartment(String departmentCode, UpdateDepartmentDto departmentDto)
            throws ApiException {
        Department department = checkIfDepartmentIsExist(departmentCode);

        department.setDepartmentName(departmentDto.getDepartmentName());

        DepartmentDto res = departmentMapper.toDto(departmentRepository.saveAndFlush(department));

        return res;
    }

    public DepartmentDto getDepartmentByCode(String departmentCode) throws ApiException {
        DepartmentDto department = departmentMapper.toDto(checkIfDepartmentIsExist(departmentCode));

        return department;
    }

    @Transactional
    public DepartmentDto deleteDepartmentByCode(String departmentCode)
            throws ApiException {
        checkIfDepartmentIsExist(departmentCode);

        DepartmentDto res = departmentMapper.toDto(departmentRepository.deleteByDepartmentCode(departmentCode));

        return res;
    }

    public Page<DepartmentDto> getAllDepartments(
            Specification<Department> filterQueries,
            Pageable paginationQueries) {
        Page<Department> departments = departmentRepository.findAll(filterQueries, paginationQueries);

        return departments.map(d -> departmentMapper.toDto(d));
    }
}
