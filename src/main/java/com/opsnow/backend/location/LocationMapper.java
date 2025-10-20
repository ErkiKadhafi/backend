package com.opsnow.backend.location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.opsnow.backend.location.dtos.LocationDto;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDto toDto(Location location);

    @Mapping(target = "employees", ignore = true)
    Location toEntity(LocationDto locationDto);
}