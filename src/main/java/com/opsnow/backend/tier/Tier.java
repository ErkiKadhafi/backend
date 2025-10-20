package com.opsnow.backend.tier;

import java.util.List;

import com.opsnow.backend.employee.Employee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tier")
public class Tier {
    @Id
    @Column(name = "tiercode")
    private Integer tierCode;

    @Column(name = "tiername")
    private String tierName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tier", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Employee> employees;
}
