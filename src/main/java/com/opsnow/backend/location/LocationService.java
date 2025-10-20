package com.opsnow.backend.location;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opsnow.backend.location.dtos.LocationDto;
import com.opsnow.backend.location.dtos.UpdateLocationDto;
import com.opsnow.backend.utils.ApiException;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    public LocationDto createlocation(LocationDto locationDto) throws ApiException {
        Optional<Location> locationOnDb = locationRepository
                .findByLocationCode(locationDto.getLocationCode());

        if (locationOnDb.isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "location with code " + locationDto.getLocationCode() + " is already exist");

        Location location = locationMapper.toEntity(locationDto);
        LocationDto res = locationMapper.toDto(locationRepository.save(location));

        return res;
    }

    public Location checkIfLocationIsExist(String locationCode) throws ApiException {
        Optional<Location> locationOnDb = locationRepository.findByLocationCode(locationCode);
        if (locationOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "location with code " + locationCode + " is not exist");

        return locationOnDb.get();
    }

    public LocationDto updatelocation(String locationCode, UpdateLocationDto locationDto)
            throws ApiException {
        Location location = checkIfLocationIsExist(locationCode);

        location.setLocationName(locationDto.getLocationName());
        location.setLocationAddress(locationDto.getLocationAddress());

        LocationDto res = locationMapper.toDto(locationRepository.saveAndFlush(location));

        return res;
    }

    public LocationDto getlocationByCode(String locationCode) throws ApiException {
        LocationDto location = locationMapper.toDto(checkIfLocationIsExist(locationCode));

        return location;
    }

    @Transactional
    public LocationDto deletelocationByCode(String locationCode)
            throws ApiException {
        checkIfLocationIsExist(locationCode);

        LocationDto res = locationMapper.toDto(locationRepository.deleteByLocationCode(locationCode));

        return res;
    }

    public Page<LocationDto> getAlllocations(
            Specification<Location> filterQueries,
            Pageable paginationQueries) {
        Page<Location> locations = locationRepository.findAll(filterQueries, paginationQueries);

        return locations.map(l -> locationMapper.toDto(l));
    }
}
