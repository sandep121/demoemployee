package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.model.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EmployeeService
{
    @Autowired
    private EmployeeDao employeeDao;

    public Collection<Employee> getAllService() {
        return this.employeeDao.getAllEmployees();
    }

    public Employee getEmployeeById(int id){
        return this.employeeDao.getEmployeeById(id);
    }

    public void addEmployee(Employee emp) {
        this.employeeDao.addEmployee(emp);
    }

    public void updateEmployee(Employee emp) {
        this.employeeDao.updateEmployee(emp);
    }

    public void deleteEmployee(int id) {
        this.employeeDao.deleteEmployee(id);
    }
}
