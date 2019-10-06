package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.Designation;

public interface EmployeeInterface {
    Designation getDesignation();

    void setDesignation(Designation designation);

    Integer getEmpId();

    void setEmpId(int empId);

    Integer getManagerId();

    void setManagerId(Integer managerId);

    String getEmpName();

    void setEmpName(String empName);
}
