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
        if( id != 0 )
        {
            return employeeRepository.findAllByManagerId(id);
        }
        else
        {
            return null;
        }
    }

    public Employee getEmployeeById(int id)
    {
        return this.findAllByEmpId(id).get(0);
    }

    public List getColleague(Integer id)
    {
        id=this.getEmployeeById(id).getManagerId();
        if( id != null )
        {
            return employeeRepository.findAllByManagerId(id);
        }
        else
        {
            return null;
        }
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
        Integer managerId=this.getEmployeeById(id).getManagerId();
        if(managerId!=null)
         {
            manager.add(this.getEmployeeById(managerId));
            return manager;
         }
        else
            return null;
    }
}
