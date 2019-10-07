package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Designation;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.DesignationRepository;
import com.sandeep.demoemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class EmployeeValidationService
{
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DesignationRepository designationRepository;

    public Boolean employeeAlreadyExists(int id)
    {
        return employeeRepository.existsAllByEmpIdIs(id);
    }

    public Boolean parentIsValid(Employee emp)
    {
        Integer managerId=emp.getManagerId();
        if(managerId!=null && employeeRepository.existsAllByEmpIdIs(managerId))
        {
            Employee manager= employeeService.findAllByEmpId(managerId).get(0);
            float managerLvl=manager.getDesignation().getLvlId();
            float empLvl=emp.getDesignation().getLvlId();
            return managerLvl < empLvl;
        }
        else return emp.getDesignation().getDsgnId() == 1;
    }

    public boolean validateDesignation(Employee employee, Designation designation)
    {
        if(employee.getDesignation().getDsgnId()==1 && designation.getDsgnId()!=1)
        {
            return  false;
        }
        List<Employee> children = employeeService.getAllByManagerId(employee.getEmpId());
        for(Employee emp : children)
        {
            if(emp.getDesignation().getLvlId()>designation.getLvlId())
                return false;
        }
        return true;
    }

    public boolean validateEntry(CrudeEmployee crudeEmployee)
    {
        Employee employee=new Employee();
        employee.setEmpId(crudeEmployee.getEmpId());
        employee.setManagerId(crudeEmployee.getManagerId());
        employee.setEmpName(crudeEmployee.getEmpName());
        employee.setDesignation(designationRepository.findAllByRoleLike(crudeEmployee.getDesignation()).get(0));
        boolean isValidEmpId= !this.employeeAlreadyExists(employee.getEmpId());
        boolean isValidParent=this.parentIsValid(employee);
        return (isValidEmpId && isValidParent);
    }

    public boolean validateManager(Employee emp, Employee newManager)
    {
        if(emp.getDesignation().getLvlId()<newManager.getDesignation().getLvlId())
        {
            return true;
        }
        return false;
    }
}
