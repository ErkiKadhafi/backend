package com.opsnow.backend.location;

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

import com.opsnow.backend.location.dtos.LocationDto;
import com.opsnow.backend.location.dtos.UpdateLocationDto;

import com.opsnow.backend.utils.ApiException;
import com.opsnow.backend.utils.PaginatedResponse;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Object> createLocation(@Valid @RequestBody LocationDto request)
            throws ApiException {
        LocationDto response = locationService.createlocation(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<Object> updateLocation(
            @PathVariable String locationCode,
            @Valid @RequestBody UpdateLocationDto request)
            throws ApiException {
        LocationDto response = locationService.updatelocation(locationCode, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<Object> getLocationByCode(@PathVariable String locationCode) throws ApiException {
        LocationDto response = locationService.getlocationByCode(locationCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{locationCode}")
    public ResponseEntity<Object> deletLocationByCode(@PathVariable String locationCode)
            throws ApiException {
        LocationDto response = locationService.deletelocationByCode(locationCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllLocations(
            @RequestParam(required = false) Integer locationCode,
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) throws ApiException {
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        Pageable paginationQueries = PageRequest.of(page, pageSize);
        Specification<Location> filterQueries = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (locationName != null && !locationName.isEmpty())
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("locationName")),
                                "%" + locationName.toLowerCase() + "%")));

            if (locationCode != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("locationCode"), locationCode));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        PaginatedResponse<LocationDto> res = new PaginatedResponse<>(
                locationService.getAlllocations(filterQueries, paginationQueries));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
