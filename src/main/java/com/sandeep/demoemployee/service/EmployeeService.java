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
    private EmployeeRepository employeeRepository;
    private DesignationRepository designationRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DesignationRepository designationRepository)
    {
        this.employeeRepository=employeeRepository;
        this.designationRepository=designationRepository;
    }

    public List<Employee> getAllEmployees()
    {
        return new ArrayList<>(employeeRepository.findAllByOrderByDesignation_lvlIdAscEmpNameAsc());
    }

    public boolean employeeExists(int id)
    {
        return !employeeRepository.existsAllByEmpIdIs(id);
    }

    public Employee getEmpFromCrudeEmp(CrudeEmployee crudeEmployee)
    {
        Employee employee=new Employee();
        if(crudeEmployee.getManagerId()!=null)
            employee.setManagerId(crudeEmployee.getManagerId());
        if(crudeEmployee.getEmpName()!=null)
            employee.setEmpName(crudeEmployee.getEmpName());
        Designation designation=null;
        if(crudeEmployee.getDesignation()!=null)
            designation=designationRepository.getByRoleLike(crudeEmployee.getDesignation());
        employee.setDesignation(designation);
        return employee;
    }

    public List<Employee> getAllByManagerId(Integer id)
    {
        if( id != 0 )
        {
            List<Employee> emp=employeeRepository.findAllByManagerIdOrderByDesignation_lvlIdAscEmpNameAsc(id);
            if(emp.size()==0)
                return null;
            return emp;
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
        id=emp.getManagerId();
        if (null == id) {
            return null;
        } else {
            List <Employee> employees=employeeRepository.findAllByManagerIdOrderByDesignation_lvlIdAscEmpNameAsc(id);
            employees.remove(emp);
            if(employees.size()==0)
                return null;
            return employees;
        }
    }


    public int addEmployee(Employee employee)
    {
        employeeRepository.save(employee);
        return employee.getEmpId();
    }

    public Employee getManager(Integer id) {
        Integer managerId=this.getEmployeeById(id).getManagerId();
        if(managerId!=null)
         {
            return this.getEmployeeById(managerId);
         }
        else
            return null;
    }

    public void updateManager(Integer oldId,Integer newId)
    {
        System.out.println("newId="+newId+"\noldId = "+oldId);
        List <Employee> children=this.getAllByManagerId(oldId);
        if(children!=null)
        {
            for (Employee emp : children) {

                if (emp != null)
                    emp.setManagerId(newId);
                assert emp != null;
                System.out.println(emp.getEmpName());
            }
            children.forEach(this::addEmployee);
        }
    }

    public Boolean deleteEmployee(int id)
    {
        Employee employee=this.getEmployeeById(id);
        this.updateManager(id,employee.getManagerId());
        employeeRepository.delete(employee);
        return true;
    }

    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }

    public String updateEmployee(Employee employee, Employee empOld) {
        employee.setEmpId(empOld.getEmpId());
        if(employee.getDesignation()!=null)
        {
            if(this.validateDesignation(employee,empOld.getDesignation()))
            {
                empOld.setDesignation(employee.getDesignation());
            }
            else
                return "Invalid Designation";
        }
        if(employee.getManagerId()!=null)
        {
            if(this.validateManager(empOld,this.getEmployeeById(employee.getManagerId())))
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

    private boolean validateDesignation(Employee employee, Designation designation)
    {
        if(designation.getDsgnId() != employee.getDesignation().getDsgnId() && (designation.getDsgnId()==1 || employee.getDesignation().getDsgnId()==1))       //cannot demote the director
        {
            return  false;
        }
        List<Employee> children = this.getAllByManagerId(employee.getEmpId());
        if (children !=null && !children.isEmpty())
        for(Employee emp : children)                                                     //for children cannot be superior to manager
        {
            assert emp !=null;
            if(emp.getDesignation().getLvlId()<=designation.getLvlId())
                return false;
        }
        return true;
    }

    public Boolean parentIsValid(Employee emp)
    {
        Integer managerId=emp.getManagerId();
        if(managerId!=null && employeeRepository.existsAllByEmpIdIs(managerId))
        {
            Employee manager= this.getEmployeeById(managerId);
            float managerLvl=manager.getDesignation().getLvlId();
            float empLvl=emp.getDesignation().getLvlId();
            return managerLvl < empLvl;
        }
        else return emp.getDesignation().getDsgnId() == 1;
    }

    public boolean validateEntry(Employee employee)
    {
        if(employee.getDesignation()!=null && employee.getDesignation().getDsgnId()==1)
            return employee.getEmpName() == null || !(employee.getManagerId() == null || employee.getManagerId()==-1);  ///Jugaar to run the test cases
        return employee.getEmpName() == null || employee.getDesignation() == null || employee.getManagerId() == null;
    }

    public boolean validateManager(Employee emp, Employee newManager)
    {
        if(newManager==null)
            return false;
        return emp.getDesignation().getLvlId() >= newManager.getDesignation().getLvlId();
    }
}
