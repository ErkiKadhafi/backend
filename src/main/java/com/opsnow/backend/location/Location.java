package com.opsnow.backend.location;

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
@Table(name = "location")
public class Location {
    @Id
    @Column(name = "locationcode")
    private String locationCode;

    @Column(name = "locationname")
    private String locationName;

    @Column(name = "locationaddress")
    private String locationAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Employee> employees;
}
