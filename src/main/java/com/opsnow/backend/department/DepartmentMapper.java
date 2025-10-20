package com.opsnow.backend.department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.opsnow.backend.department.dtos.DepartmentDto;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDto toDto(Department department);

    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentDto departmentDto);
}
