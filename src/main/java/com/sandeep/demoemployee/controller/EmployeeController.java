package com.sandeep.demoemployee.controller;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.NewEmployee;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sandeep.demoemployee.util.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@Api
public class EmployeeController
{
    private EmployeeService employeeService;
    private Messages messages;
    @Autowired
    public EmployeeController(EmployeeService employeeService, Messages messages) {
        this.employeeService = employeeService;
        this.messages = messages;
    }

    @ApiOperation(value = "View a list of available employees", response = List.class)
    @GetMapping
    public ResponseEntity getAllEmployee()
    {
        List <Employee> allEmployees=new ArrayList<>(employeeService.getAllEmployees());
        return new ResponseEntity<>(allEmployees,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findAllByEmpId(@PathVariable int id)
    {
        if(id<1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(employeeService.employeeExists(id))
            return new ResponseEntity<>( messages.getMessage("EMPLOYEE_NOT_FOUND"),HttpStatus.NOT_FOUND);
        Object subOrdinates=employeeService.getAllByManagerId(id);
        Object colleague=employeeService.getColleague(id);
        Object manager=employeeService.getManager(id);
        Map<String,Object> employeeResponse=new HashMap<>();
        employeeResponse.put("employee", employeeService.getEmployeeById(id));
        if(colleague!=null)
            employeeResponse.put("colleagues", colleague);
        if(manager!=null)
            employeeResponse.put("manager", manager);
        if(subOrdinates!=null)
            employeeResponse.put("subordinates", subOrdinates );
        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }
    @GetMapping("/CountDsgId={id}")
    public ResponseEntity countByDesignationId(@PathVariable int id)
    {
        return new ResponseEntity<>(employeeService.getTotalEmployeeByDesignation(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        //crude is needed for designation field
        if(crudeEmployee.getEmpName()==null || crudeEmployee.getEmpName().matches(".*\\d.*"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        if(employeeService.validateEntry(employee))
            return new ResponseEntity(employee, HttpStatus.BAD_REQUEST);
        boolean isValidParent=employeeService.parentIsValid(employee);
        if(employee.getDesignation().getDsgnId()==1 && employeeService.getTotalEmployeeByDesignation(1)!=0)
        {
            return new ResponseEntity<>("One Director is enough for the organisation", HttpStatus.BAD_REQUEST);
        }
        else if(!isValidParent)
        {
            return new ResponseEntity<>("Invalid superior", HttpStatus.BAD_REQUEST);
        }
        else
        return new ResponseEntity(employeeService.getEmployeeById(employeeService.addEmployee(employee)),HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@RequestBody NewEmployee crudeEmployee, @PathVariable int id)
    {
        if(id<1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(crudeEmployee.getEmpName()!=null&&crudeEmployee.getEmpName().matches(".*\\d.*"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Employee empOld=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        if(employeeService.employeeExists(id))                              //check if the employee exists or not
        {
            return new ResponseEntity<>("Employee not found", HttpStatus.BAD_REQUEST);
        }
        else if(crudeEmployee.isReplace())
        {
            Employee empNew=new Employee(empOld);
            if(empNew.getManagerId()==null)
                empNew.setManagerId(employeeService.getEmployeeById(id).getManagerId());
            if(employeeService.validateEntry(empNew))                    //to check if all the required fields are available
            return new ResponseEntity<>("missing fields",HttpStatus.BAD_REQUEST);

            if(employeeService.getEmployeeById(id).getDesignation().getDsgnId() != empNew.getDesignation().getDsgnId()
                    && (empNew.getDesignation().getDsgnId()==1
                    || employeeService.getEmployeeById(id).getDesignation().getDsgnId() == 1))
                return new ResponseEntity<>("There should be one and only one Director in the organisation", HttpStatus.BAD_REQUEST);


            if(employeeService.parentIsValid(empNew))
            {
                empNew.setEmpId(employeeService.addEmployee(empNew));
                //employeeService.updateManager(id,empNew.getEmpId());
                employeeService.deleteEmployee(id);
                return this.findAllByEmpId(empNew.getEmpId());
            }
            else
                return new ResponseEntity<>("organisation hierarchy violated",HttpStatus.BAD_REQUEST);
        }


        else
        {
            String result;
            result=employeeService.updateEmployee(empOld, employeeService.getEmployeeById(id));
            if(result==null)
                return this.findAllByEmpId(id);
            else
                return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id)
    {
        if(id<1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(employeeService.employeeExists(id))
        {
            return new ResponseEntity<>("The employee does not exist", HttpStatus.NOT_FOUND);
        }
        else if(employeeService.getEmployeeById(id).getDesignation().getDsgnId()==1 && employeeService.getTotalEmployeeCount()!=1)
        {

            return new ResponseEntity<>("You cannot fire the director!!!",HttpStatus.BAD_REQUEST);
        }
        else if(employeeService.deleteEmployee(id))
        {
            return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}