package com.opsnow.backend.location;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String>, JpaSpecificationExecutor<Location> {
    Optional<Location> findByLocationCode(String locationCode);

    Location deleteByLocationCode(String locationCode);
}
