package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.Designation;

public interface EmployeeInterface {
    Designation getDesignation();

    void setDesignation(Designation designation);

    int getEmpId();

    void setEmpId(int empId);

    int getManagerId();

    void setManagerId(int managerId);

    String getEmpName();

    void setEmpName(String empName);
}
