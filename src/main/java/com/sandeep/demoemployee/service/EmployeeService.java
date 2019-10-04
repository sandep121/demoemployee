package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.DesignationRepository;
import com.sandeep.demoemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService
{
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DesignationRepository designationRepository;

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

    public Employee getEmpFromCrudeEmp(CrudeEmployee crudeEmployee)
    {
        Employee employee=new Employee();
        employee.setEmpId(crudeEmployee.getEmpId());
        employee.setManagerId(crudeEmployee.getManagerId());
        employee.setEmpName(crudeEmployee.getEmpName());
        employee.setDesignation(designationRepository.findAllByRoleLike(crudeEmployee.getDesignation().toUpperCase()).get(0));
        return employee;
    }

    public List getAllByManagerId(int id)
    {
        return employeeRepository.findAllByManagerId(id);
    }

    public Employee getEmployeeById(int id)
    {
        return this.findAllByEmpId(id).get(0);
    }

    public List getColleague(int id)
    {
        return employeeRepository.findAllByManagerId(this.getEmployeeById(id).getManagerId());
    }


    public List<Employee> findAllByEmpId(int id)
    {
        List <Employee> emp=new ArrayList<>();
        emp.add(employeeRepository.findById(id).orElseGet(Employee::new));
        return emp;
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getManager(int id) {
        List <Employee> manager=new ArrayList<>();
        manager.add(this.getEmployeeById(this.getEmployeeById(id).getManagerId()));
        return manager;
    }
}
