package com.opsnow.backend.tier;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.opsnow.backend.tier.dtos.TierDto;

@Mapper(componentModel = "spring")
public interface TierMapper {
    TierMapper INSTANCE = Mappers.getMapper(TierMapper.class);

    TierDto toDto(Tier Tier);

    @Mapping(target = "employees", ignore = true)
    Tier toEntity(TierDto TierDto);
}