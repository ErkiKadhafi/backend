package com.opsnow.backend.tier;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opsnow.backend.tier.dtos.TierDto;
import com.opsnow.backend.tier.dtos.UpdateTierDto;
import com.opsnow.backend.utils.ApiException;

@Service
public class TierService {
    private final TierRepository tierRepository;
    private final TierMapper tierMapper;

    public TierService(TierRepository tierRepository, TierMapper tierMapper) {
        this.tierRepository = tierRepository;
        this.tierMapper = tierMapper;
    }

    public Tier checkIfTierIsExist(Integer tierCode) throws ApiException {
        Optional<Tier> tierOnDb = tierRepository.findByTierCode(tierCode);
        if (tierOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "tier with code " + tierCode + " is not exist");

        return tierOnDb.get();
    }

    public TierDto creatTier(TierDto tierDto) throws ApiException {
        Optional<Tier> tierOnDb = tierRepository
                .findByTierCode(tierDto.getTierCode());

        if (tierOnDb.isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "tier with code " + tierDto.getTierCode() + " is already exist");

        Tier tier = tierMapper.toEntity(tierDto);
        TierDto res = tierMapper.toDto(tierRepository.save(tier));

        return res;
    }

    public TierDto updateTier(Integer tierCode, UpdateTierDto tierDto)
            throws ApiException {
        Tier tier = checkIfTierIsExist(tierCode);

        tier.setTierName(tierDto.getTierName());

        TierDto res = tierMapper.toDto(tierRepository.saveAndFlush(tier));

        return res;
    }

    public TierDto getTierByCode(Integer tierCode) throws ApiException {
        TierDto tier = tierMapper.toDto(checkIfTierIsExist(tierCode));

        return tier;
    }

    @Transactional
    public TierDto deleteTierByCode(Integer tierCode)
            throws ApiException {
        checkIfTierIsExist(tierCode);

        TierDto res = tierMapper.toDto(tierRepository.deleteByTierCode(tierCode));

        return res;
    }

    public Page<TierDto> getAllTiers(
            Specification<Tier> filterQueries,
            Pageable paginationQueries) {
        Page<Tier> tiers = tierRepository.findAll(filterQueries, paginationQueries);

        return tiers.map(t -> tierMapper.toDto(t));
    }
}
