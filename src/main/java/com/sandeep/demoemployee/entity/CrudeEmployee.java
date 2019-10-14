package com.sandeep.demoemployee.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrudeEmployee
{
    private Integer managerId;
    @JsonProperty("name")
    private String empName;
    @JsonProperty("jobTitle")
    private String designation;
    public CrudeEmployee(Integer managerId, String empName, String designation) {
        this.managerId = managerId;
        this.empName = empName;
        this.designation = designation;
    }

    public CrudeEmployee(Integer managerId, String empName) {
        this.managerId = managerId;
        this.empName = empName;
    }

    public CrudeEmployee() {
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
                ", managerId=" + managerId +
                ", empName='" + empName + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
