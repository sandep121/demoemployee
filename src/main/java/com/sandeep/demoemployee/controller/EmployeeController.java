package com.sandeep.demoemployee.controller;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.NewEmployee;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sandeep.demoemployee.service.EmployeeValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@Api
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeValidationService validationService;


    @ApiOperation(value = "View a list of available employees", response = List.class)
    @GetMapping("/")
    public List<Employee> getAllEmployee()
    {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity findAllByEmpId(@PathVariable int id)
    {
        if(!employeeService.employeeExists(id))
            return new ResponseEntity<>( "Employee does not exist",HttpStatus.NOT_FOUND);
        Map<String,List<Employee>> employeeResponse=new HashMap<>();
        employeeResponse.put("Employee", employeeService.findAllByEmpId(id));
        employeeResponse.put("Colleague", employeeService.getColleague(id));
        employeeResponse.put("Manager", employeeService.getManager(id));
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
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        if(!validationService.validateEntry(employee))
            return new ResponseEntity<>("missing or invalid values", HttpStatus.BAD_REQUEST);
        boolean isValidParent=validationService.parentIsValid(employee);
        if(employee.getDesignation().getDsgnId()==1 && employeeService.getTotalEmployeeByDesignation(1)!=0)
        {
            return new ResponseEntity<>("One Director is enough for the organisation", HttpStatus.FORBIDDEN);
        }
        else if(!isValidParent)
        {
            return new ResponseEntity<>("Invalid superior", HttpStatus.BAD_REQUEST);
        }

        else
            employeeService.addEmployee(employee);

        return new ResponseEntity<>("Employee added successfully",HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@RequestBody NewEmployee crudeEmployee, @PathVariable int id)
    {
        Employee empOld=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        if(!employeeService.employeeExists(id))                              //check if the employee exists or not
        {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }

        else if(crudeEmployee.isReplace())
        {
            Employee empNew=new Employee(empOld);
            if(employeeService.getEmployeeById(id).getDesignation().getDsgnId() != empNew.getDesignation().getDsgnId()
                    && (empNew.getDesignation().getDsgnId()==1
                    || employeeService.getEmployeeById(id).getDesignation().getDsgnId() == 1))
                return new ResponseEntity<>("There should be one and only one Director in the organisation", HttpStatus.BAD_REQUEST);

            if(!validationService.validateEntry(empNew))
                return new ResponseEntity<>("missing fields",HttpStatus.BAD_REQUEST);

            if(validationService.parentIsValid(empNew))
            {
                employeeService.updateManager(id,employeeService.addEmployee(empNew));
                employeeService.deleteEmployee(id);
                return new ResponseEntity<>("Employee replaced successfully",HttpStatus.OK);
            }
            else
                return new ResponseEntity<>("organisation hierarchy violated",HttpStatus.BAD_REQUEST);
        }


        else
        {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            String result=employeeService.updateEmployee(empOld, employeeService.getEmployeeById(id));
            if(result==null)
                return new ResponseEntity<>("Employee updated successfully",HttpStatus.OK);
            else
                return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id)
    {
        if(!employeeService.employeeExists(id))
        {
            return new ResponseEntity<>("The employee does not exist", HttpStatus.NOT_FOUND);
        }
        else if(employeeService.getEmployeeById(id).getDesignation().getDsgnId()==1 && employeeService.getTotalEmployee()!=1)
        {

            return new ResponseEntity<>("You cannot fire the director!!!",HttpStatus.FORBIDDEN);
        }
        else if(employeeService.deleteEmployee(id))
        {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}