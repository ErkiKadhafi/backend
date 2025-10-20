package com.opsnow.backend.tier;

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

import com.opsnow.backend.tier.dtos.TierDto;
import com.opsnow.backend.tier.dtos.UpdateTierDto;
import com.opsnow.backend.utils.ApiException;
import com.opsnow.backend.utils.PaginatedResponse;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/tiers")
public class TierController {
    private final TierService tierService;

    public TierController(TierService tierService) {
        this.tierService = tierService;
    }

    @PostMapping
    public ResponseEntity<Object> createTier(@Valid @RequestBody TierDto request)
            throws ApiException {
        TierDto response = tierService.creatTier(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{tierCode}")
    public ResponseEntity<Object> updateTier(
            @PathVariable Integer tierCode,
            @Valid @RequestBody UpdateTierDto request)
            throws ApiException {
        TierDto response = tierService.updateTier(tierCode, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{tierCode}")
    public ResponseEntity<Object> getTierByCode(@PathVariable Integer tierCode) throws ApiException {
        TierDto response = tierService.getTierByCode(tierCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{tierCode}")
    public ResponseEntity<Object> deletTierByCode(@PathVariable Integer tierCode)
            throws ApiException {
        TierDto response = tierService.deleteTierByCode(tierCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllTiers(
            @RequestParam(required = false) Integer tierCode,
            @RequestParam(required = false) String tierName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) throws ApiException {
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        Pageable paginationQueries = PageRequest.of(page, pageSize);
        Specification<Tier> filterQueries = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (tierName != null && !tierName.isEmpty())
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("tierName")),
                                "%" + tierName.toLowerCase() + "%")));

            if (tierCode != null)
                predicates.add(
                        criteriaBuilder.equal(root.get("tierCode"), tierCode));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        PaginatedResponse<TierDto> res = new PaginatedResponse<>(
                tierService.getAllTiers(filterQueries, paginationQueries));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
