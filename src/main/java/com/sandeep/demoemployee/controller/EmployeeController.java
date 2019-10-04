package com.sandeep.demoemployee.controller;

//import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.DesignationRepository;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sandeep.demoemployee.service.EmployeeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeValidationService validationService;

    @RequestMapping("/")
    public List getAllEmployee()
    {
        return employeeService.getAllEmployees();
    }

    @RequestMapping("/{id}")
    public ResponseEntity findAllByEmpId(Model model, @PathVariable int id)
    {
        Map<String,List<Employee>> employeeResponse=new HashMap<>();
        employeeResponse.put("Employee", employeeService.findAllByEmpId(id));
        employeeResponse.put("Colleague", employeeService.getColleague(id));
        employeeResponse.put("Manager", employeeService.getManager(id));
        if(employeeService.employeeExists(id))
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        else
            return new ResponseEntity<>("Employee Does Not Exit", HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity addEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        boolean isValidEmpId= !validationService.employeeAlreadyExists(employee.getEmpId());
        boolean isValidParent=validationService.parentIsValid(employee);
        if(!isValidEmpId)
        {
            return new ResponseEntity<>("Employee already exists", HttpStatus.CONFLICT);
        }
        else if(!isValidParent)
        {
            return new ResponseEntity("Organisation hierarchy violated", HttpStatus.CONFLICT);
        }
        else
            employeeService.addEmployee(employee);

        return new ResponseEntity<>("we are working on it",HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity updateEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        if(!crudeEmployee.isReplace())
        {
            return this.addEmployee(crudeEmployee);
        }
        else if(!employeeService.employeeExists(employee.getEmpId()))
        {
            return new ResponseEntity("Employee not found", HttpStatus.NOT_FOUND);
        }
        else
        {
        }
        return new ResponseEntity("we are working on it", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable int id)
    {
//        if(employeeService.employeeExists(id))
//        {
//            List<Employee> children = employeeService.
//        }
        return new ResponseEntity("we are working on it", HttpStatus.OK);
    }
}