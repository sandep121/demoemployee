package com.sandeep.demoemployee.entity;

import com.fasterxml.jackson.annotation.*;
import com.sandeep.demoemployee.service.EmployeeInterface;
import org.hibernate.annotations.ManyToAny;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
public class Employee implements EmployeeInterface {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int empId;
    @Column
    @Nullable
    private Integer managerId;
    @Column
    private String empName;
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

    public Employee(int empId, int managerId, String empName) {
        this.empId = empId;
        this.managerId = managerId;
        this.empName = empName;
    }

    public Employee(Employee emp)
    {
        this.setDesignation(emp.getDesignation());
        this.setEmpName(emp.getEmpName());
        this.setEmpId(emp.getEmpId());
        this.setManagerId(emp.getManagerId());
    }

    public Employee(int empId, int managerId, String empName, Designation designation) {
        this.empId = empId;
        this.managerId = managerId;
        this.empName = empName;
        this.designation = designation;
    }

    public Employee() {
    }

    @Override
    public int getEmpId() {
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
