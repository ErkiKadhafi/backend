package com.opsnow.backend.tier;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<Tier, Integer>, JpaSpecificationExecutor<Tier> {
    Optional<Tier> findByTierCode(Integer tierCode);

    Tier deleteByTierCode(Integer tierCode);
}
