package com.opsnow.backend.department;

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
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "departmentcode")
    private String departmentCode;

    @Column(name = "departmentname")
    private String departmentName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Employee> employees;
}
