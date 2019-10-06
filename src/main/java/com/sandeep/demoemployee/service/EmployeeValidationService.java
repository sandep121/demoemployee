package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.DesignationRepository;
import com.sandeep.demoemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
            Employee manager=(Employee) employeeService.findAllByEmpId(managerId).get(0);
            float managerLvl=manager.getDesignation().getLvlId();
            float empLvl=emp.getDesignation().getLvlId();
            return managerLvl < empLvl;
        }
        else return emp.getDesignation().getDsgnId() == 1;
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
}
