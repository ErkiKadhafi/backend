package com.opsnow.backend.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.opsnow.backend.employee.dtos.EmployeeCreateDto;
import com.opsnow.backend.employee.dtos.EmployeeDto;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDto toDto(Employee employee);

    @Mapping(target = "entryDate", ignore = true)
    @Mapping(target = "tier.tierCode", source = "tierCode")
    @Mapping(target = "department.departmentCode", source = "departmentCode")
    @Mapping(target = "location.locationCode", source = "locationCode")
    Employee employeeCreateDtoToEmployee(EmployeeCreateDto employeeCreateDto);

    EmployeeDto employeeDto(Employee employee);

}