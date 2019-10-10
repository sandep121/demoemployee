package com.sandeep.demoemployee.service;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Designation;
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
    @Autowired
    private EmployeeValidationService employeeValidationService;
    public List<Employee> getAllEmployees()
    {
        return new ArrayList<>(employeeRepository.findAllByOrderByDesignation_lvlIdAscEmpNameAsc());
    }

    public boolean employeeExists(int id)
    {
        return employeeRepository.existsAllByEmpIdIs(id);
    }

    public Employee getEmpFromCrudeEmp(CrudeEmployee crudeEmployee)
    {
        Employee employee=new Employee();
        employee.setManagerId(crudeEmployee.getManagerId());
        employee.setEmpName(crudeEmployee.getEmpName());
        Designation designation=null;
        if(crudeEmployee.getDesignation()!=null)
            designation=designationRepository.getByRoleLike(crudeEmployee.getDesignation().toUpperCase());
        employee.setDesignation(designation);
        return employee;
    }

    public List<Employee> getAllByManagerId(int id)
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

    public Long getTotalEmployeeByDesignation(Integer id)
    {
        return employeeRepository.countAllByDesignation_DsgnId(id);
    }

    public Employee getEmployeeById(int id)
    {
        return employeeRepository.findByEmpId(id);
    }

    public List<Employee> getColleague(Integer id)
    {
        Employee emp=this.getEmployeeById(id);
        if (null == emp.getManagerId()) {
            return null;
        } else {
            List <Employee> employees=employeeRepository.findAllByManagerId(id);
            employees.remove(emp);
            return employees;
        }
    }


    public List<Employee> findAllByEmpId(int id)
    {
        List <Employee> emp=new ArrayList<>();
        emp.add(employeeRepository.findById(id).orElseGet(Employee::new));
        return emp;
    }
    public int addEmployee(Employee employee)
    {
        employeeRepository.save(employee);
        System.out.println(employee.getEmpId());
        return employee.getEmpId();
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

    public void updateManager(Integer oldId,Integer newId)
    {
        List <Employee> children=this.getAllByManagerId(oldId);
        assert children != null;
        children.forEach((emp)->emp.setManagerId(newId));
        children.forEach(this::addEmployee);
    }

    public Boolean deleteEmployee(int id)
    {
        Employee employee=this.getEmployeeById(id);
        this.updateManager(id,employee.getManagerId());
        employeeRepository.delete(employee);
        return true;
    }

    public long getTotalEmployee() {
        return employeeRepository.count();
    }

    public String updateEmployee(Employee employee, Employee empOld) {
        if(employee.getDesignation()!=null)
        {
            if(employeeValidationService.validateDesignation(empOld,employee.getDesignation()))
            {
                empOld.setDesignation(employee.getDesignation());
            }
            else
                return "Invalid Designation";
        }
        if(employee.getManagerId()!=null)
        {
            if(employeeValidationService.validateManager(empOld,this.getEmployeeById(employee.getManagerId())))
                empOld.setManagerId(employee.getManagerId());
            else
                return "Invalid Manager";
        }
        if(employee.getEmpName()!=null)
        {
            empOld.setEmpName(employee.getEmpName());
            System.out.println(empOld.getEmpName());
        }
        employeeRepository.save(empOld);
        return null;
    }
}
