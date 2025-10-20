package com.opsnow.backend.employee;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.opsnow.backend.department.Department;
import com.opsnow.backend.location.Location;
import com.opsnow.backend.tier.Tier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @Column(name = "empno")
    private Integer empNo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tiercode", nullable = false)
    private Tier tier;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "locationcode", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "departmentcode", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "supervisorcode", nullable = true)
    private Employee supervisor;

    // @Column(name = "supervisorcode")
    // private Integer supervisorCode;

    @Column(name = "empname")
    private String empName;

    @Column(name = "salary")
    private Integer salary;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entrydate")
    private LocalDateTime entryDate;
}
