package com.sandeep.demoemployee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

public class CrudeEmployee
{
    @JsonIgnore
    private Integer empId;
    private Integer managerId;
    private String empName;
    private String designation;
    public CrudeEmployee(Integer empId, Integer managerId, String empName, String designation) {
        this.empId = empId;
        this.managerId = managerId;
        this.empName = empName;
        this.designation = designation;
    }

    public CrudeEmployee() {
    }

    public CrudeEmployee(int managerId, String empName, String designation) {
        this.managerId = managerId;
        this.empName = empName;
        this.designation = designation;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "CrudeEmployee{" +
                "empId=" + empId +
                ", managerId=" + managerId +
                ", empName='" + empName + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
