package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.EmployeeRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService
{
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees()
    {
        List employee=new ArrayList<Employee>();
        employeeRepository.findAllByOrderByDesignation_lvlIdAscEmpNameAsc().forEach(employee::add);
        return employee;
    }

    public boolean employeeExists(int id)
    {
        return employeeRepository.existsAllByEmpIdIs(id);
    }

    public Employee findAllByEmpId(int id)
    {

        return employeeRepository.findById(id).orElseGet(Employee::new);
    }
}
