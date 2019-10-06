package com.sandeep.demoemployee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sandeep.demoemployee.service.EmployeeInterface;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
public class Employee implements EmployeeInterface {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer empId;
    @Column
    @Nullable
    private Integer managerId;
    @Column
    private String empName;
    @Column
    @Nullable
    @JsonIgnore
    private Short uniqueId;
    @ManyToOne
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

    public Employee(@Nullable Integer managerId, String empName, @Nullable Short uniqueId, Designation designation) {
        this.managerId = managerId;
        this.empName = empName;
        this.uniqueId = uniqueId;
        this.designation = designation;
    }

    public Employee(Employee emp) {
        this.empId = emp.getEmpId();
        this.managerId = emp.getManagerId();
        this.empName = emp.getEmpName();
        this.uniqueId = emp.getUniqueId();
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

    @Nullable
    public Short getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(@Nullable Short uniqueId) {
        this.uniqueId = uniqueId;
    }
}
