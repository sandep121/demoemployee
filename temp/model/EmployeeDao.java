package com.sandeep.demoemployee.model;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EmployeeDao
{
    private static Map<Integer, Employee> employees;

    static{
        employees = new HashMap<Integer, Employee>(){
            {
                put(1, new Employee( "Thor",1, 1, 1));
                put(2, new Employee( "Iron Man",2, 1, 2));
                put(3, new Employee( "Spiderman",3, 2, 6));
                put(4, new Employee( "Captain America",4, 1, 2));
            }
        };
    }

    public Collection<Employee> getAllEmployees(){
        return this.employees.values();
    }

    public Employee getEmployeeById(int id) {
        return this.employees.get(id);
    }

    public void addEmployee(Employee emp) {
        this.employees.put(emp.getId(),emp);
    }

    public void updateEmployee(Employee emp) {
        Employee employee = this.getEmployeeById(emp.getId());
        employee.setName(emp.getName());
        employee.setManagerId(emp.getManagerId());
        employee.setRoleId(emp.getRoleId());
        this.employees.put(employee.getId(),employee);
    }

    public void deleteEmployee(int id) {
        this.employees.remove(id);
    }
}
