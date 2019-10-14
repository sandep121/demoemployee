package com.sandeep.demoemployee.entity;

import com.fasterxml.jackson.annotation.*;
import com.sandeep.demoemployee.service.EmployeeInterface;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
public class Employee implements EmployeeInterface {
    @Id
    @JsonProperty(value = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer empId;
    @Column
    @Nullable
    @JsonIgnore
    private Integer managerId;
    @Column
    @JsonProperty(value = "name")
    private String empName;
    @ManyToOne
    @JsonUnwrapped
    @JoinColumn(name = "DSGN_ID")
    private Designation designation;

    @Override
    public Designation getDesignation() {
        return designation;
    }

    @Override
    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Employee(@Nullable Integer managerId, String empName, Designation designation) {
        this.managerId = managerId;
        this.empName = empName;
        this.designation = designation;
    }

    public Employee(Employee emp) {
        this.empId = emp.getEmpId();
        this.managerId = emp.getManagerId();
        this.empName = emp.getEmpName();
        this.designation = emp.getDesignation();
    }



    public Employee() {
    }

    @Override
    public Integer getEmpId() {
        return empId;
    }

    @Override
    public void setEmpId(int empId) {
        this.empId = empId;
    }

    @Override
    public Integer getManagerId() {
        return managerId;
    }

    @Override
    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String getEmpName() {
        return empName;
    }

    @Override
    public void setEmpName(String empName) {
        this.empName = empName;
    }

}
