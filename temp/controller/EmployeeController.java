package com.sandeep.demoemployee.controller;

import com.sandeep.demoemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Employee> getAllEmployees(){
        return employeeService.getAllService();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Employee getEmployeeById(@PathVariable("id") int id)
    {
        return employeeService.getEmployeeById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addEmployee(@RequestBody Employee emp)
    {
        employeeService.addEmployee(emp);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateEmployee(@RequestBody Employee emp)
    {
        employeeService.updateEmployee(emp);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public void deleteEmployee(@PathVariable("id") int id)
    {
        employeeService.deleteEmployee(id);
    }
}