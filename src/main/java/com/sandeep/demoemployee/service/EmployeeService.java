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
        return new ArrayList<>(employeeRepository.findAllByOrderByDesignation_lvlIdAscEmpNameAsc());
    }

    public boolean employeeExists(int id)
    {
        return employeeRepository.existsAllByEmpIdIs(id);
    }

    public Employee getEmpFromCrudeEmp(CrudeEmployee crudeEmployee)
    {
        Employee employee=new Employee();
        if(crudeEmployee.getEmpId()==null)
            employee.setEmpId(0);
        else
            employee.setEmpId(crudeEmployee.getEmpId());
        employee.setManagerId(crudeEmployee.getManagerId());
        employee.setEmpName(crudeEmployee.getEmpName());
        employee.setDesignation(designationRepository.findAllByRoleLike(crudeEmployee.getDesignation().toUpperCase()).get(0));
        return employee;
    }

    private List<Employee> getAllByManagerId(int id)
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

    public List<Employee> getColleague(Integer id)
    {
        id=this.getEmployeeById(id).getManagerId();
        if (null == id) {
            return null;
        } else {
            return employeeRepository.findAllByManagerId(id);
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
        short random = (short) (Math.random());
        employee.setUniqueId(random);
        employeeRepository.save(employee);
        employee=this.getEmployeeByUniqueId(random);
        employee.setUniqueId(null);
        employeeRepository.save(employee);
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

    public Employee getEmployeeByUniqueId(short random)
    {
        return employeeRepository.findByUniqueId(random);
    }

    public long getTotalEmployee() {
        return employeeRepository.count();
    }
}
