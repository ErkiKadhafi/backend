package com.opsnow.backend.department;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {
    Optional<Department> findByDepartmentCode(String departmentCode);

    Department deleteByDepartmentCode(String departmentCode);
}
